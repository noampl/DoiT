package com.example.doit.view.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doit.R;
import com.example.doit.databinding.BaseModelItemBinding;
import com.example.doit.model.entities.User;
import com.example.doit.viewmodel.UsersViewModel;

public class AdditionAdapter extends ListAdapter<User, AdditionAdapter.AdditionViewHolder> {

    // Members

    private UsersViewModel _usersViewModel;

    // endregion

    public AdditionAdapter() {
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
    }

    public void set_usersViewModel(UsersViewModel _usersViewModel) {
        this._usersViewModel = _usersViewModel;
    }

    @NonNull
    @Override
    public AdditionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseModelItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.base_model_item, parent, false);
        return new AdditionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdditionViewHolder holder, int position) {
        holder.bind(getItem(position), _usersViewModel);
    }

    public static class AdditionViewHolder extends RecyclerView.ViewHolder {

        private BaseModelItemBinding _binding;

        public AdditionViewHolder(@NonNull BaseModelItemBinding binding) {
            super(binding.getRoot());
            _binding = binding;
        }

        public void bind(User user, UsersViewModel viewModel) {
            _binding.setUser(user);
            _binding.setPosition(getAdapterPosition());
            _binding.setUsersViewModel(viewModel);
            _binding.executePendingBindings();
        }
    }
}
