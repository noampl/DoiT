package com.example.doit.view.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doit.R;
import com.example.doit.databinding.BaseModelItemBinding;
import com.example.doit.model.entities.User;
import com.example.doit.viewmodel.UsersViewModel;

import java.util.List;

public class AdditionAdapter extends ListAdapter<User, AdditionAdapter.AdditionViewHolder> {

    // Members

    private UsersViewModel _usersViewModel;
    private final boolean isChecked;
    private final boolean isMultiplechoice;
    private final LifecycleOwner _lifeCycleOwner;

    // endregion

    public AdditionAdapter(boolean isChecked, LifecycleOwner lifecycleOwner, boolean isMultiplechoice) {
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
        this.isChecked = isChecked;
        _lifeCycleOwner = lifecycleOwner;
        this.isMultiplechoice = isMultiplechoice;
    }

    public void set_usersViewModel(UsersViewModel _usersViewModel) {
        this._usersViewModel = _usersViewModel;
    }

    @NonNull
    @Override
    public AdditionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseModelItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.base_model_item, parent, false);
        return new AdditionViewHolder(binding, isChecked, _lifeCycleOwner, isMultiplechoice);
    }

    @Override
    public void onBindViewHolder(@NonNull AdditionViewHolder holder, int position) {
        holder.bind(getItem(position), _usersViewModel);
    }

    public static class AdditionViewHolder extends RecyclerView.ViewHolder {

        private final BaseModelItemBinding _binding;
        private boolean isChecked;
        private final LifecycleOwner lifecycleOwner;
        private final boolean isMultipleChoice;

        public AdditionViewHolder(@NonNull BaseModelItemBinding binding, boolean isChecked,
                                  LifecycleOwner lifecycleOwner,boolean isMultiplechoice) {
            super(binding.getRoot());
            _binding = binding;
            this.isChecked = isChecked;
            this.lifecycleOwner = lifecycleOwner;
            this.isMultipleChoice= isMultiplechoice;
        }

        public void bind(User user, UsersViewModel viewModel) {
            viewModel.get_selectedCheckBoxPosition().observe(lifecycleOwner, new Observer<List<Integer>>() {
                    @Override
                    public void onChanged(List<Integer> integers) {
                        boolean tmp = false;
                        for (int index : integers) {
                            if (index == getAdapterPosition()){
                                tmp = true;
                                break;
                            }
                        }
                      _binding.setIsChecked(tmp);
                    }
                });
            _binding.setUser(user);
            _binding.setPosition(getAdapterPosition());
            _binding.setUsersViewModel(viewModel);
            _binding.setIsChecked(isChecked);
            _binding.setMultipleChoice(isMultipleChoice);
            _binding.executePendingBindings();
        }
    }
}
