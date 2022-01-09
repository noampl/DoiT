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

public class AdditionAdapter extends ListAdapter<User, AdditionAdapter.AdditionViewHolder> {

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

    @NonNull
    @Override
    public AdditionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseModelItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.base_model_item, parent, false);
        return new AdditionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdditionViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static class AdditionViewHolder extends RecyclerView.ViewHolder {

        private BaseModelItemBinding _binding;

        public AdditionViewHolder(@NonNull BaseModelItemBinding binding) {
            super(binding.getRoot());
            _binding = binding;
        }

        public void bind(User user) {
            _binding.setUser(user);
            _binding.executePendingBindings();
        }
    }
}
