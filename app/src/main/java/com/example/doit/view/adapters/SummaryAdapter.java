package com.example.doit.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doit.R;
import com.example.doit.databinding.DialogSummaryBinding;
import com.example.doit.databinding.SummaryItemBinding;
import com.example.doit.model.entities.User;
import com.example.doit.viewmodel.UsersViewModel;

import java.util.concurrent.CompletableFuture;

public class SummaryAdapter extends ListAdapter<User, SummaryAdapter.SummaryViewHolder> {

    // region Members

    private UsersViewModel _usersViewModel;
    private String _groupId;

    // endregion

    // region C'tor

    public SummaryAdapter(String _groupId) {
        super(new DiffUtil.ItemCallback<User>() {
            @Override
            public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
                return oldItem.get_userId().equals(newItem.get_userId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
                return oldItem.equals(newItem);
            }
        });
        this._groupId = _groupId;
    }

    // endregion

    // region Properties

    public void set_usersViewModel(UsersViewModel _usersViewModel) {
        this._usersViewModel = _usersViewModel;
    }

    // endregion

    // region ViewHolder

    @NonNull
    @Override
    public SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SummaryItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.summary_item, parent, false);
        return new SummaryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryViewHolder holder, int position) {
        holder.bind(getItem(position), _usersViewModel.getUserScore(getItem(position), _groupId));
    }

    public static class SummaryViewHolder extends RecyclerView.ViewHolder {

        private SummaryItemBinding _binding;

        public SummaryViewHolder(@NonNull SummaryItemBinding binding) {
            super(binding.getRoot());
            _binding = binding;
}

        public void bind(User user, CompletableFuture<Integer> score) {
            _binding.setUser(user);
            score.thenAccept((score1)-> _binding.setScore(score1));
            _binding.executePendingBindings();
        }
    }

    // endregion
}
