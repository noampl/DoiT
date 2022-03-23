package com.example.doit.view.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doit.R;
import com.example.doit.databinding.GroupItemBinding;
import com.example.doit.model.entities.Group;
import com.example.doit.viewmodel.GroupsViewModel;

public class GroupsAdapter extends ListAdapter<Group, GroupsAdapter.GroupsViewHolder>{

    // region Members

    private GroupsViewModel _groupsViewModel;
    private LifecycleOwner _lifecycleOwner;

    // endregion

    // region C'tor

    public GroupsAdapter(GroupsViewModel groupsViewModel, LifecycleOwner lifecycleOwner) {
        super(new DiffUtil.ItemCallback<Group>() {
            @Override
            public boolean areItemsTheSame(@NonNull Group oldItem, @NonNull Group newItem) {
                return oldItem.get_groupId().equals(newItem.get_groupId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Group oldItem, @NonNull Group newItem) {
                return oldItem.equals(newItem);
            }
        });
        _groupsViewModel = groupsViewModel;
        _lifecycleOwner = lifecycleOwner;
    }

    // endregion

    // region Properties

    // endregion

    // region RecyclerAdapter

    @NonNull
    @Override
    public GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GroupItemBinding _binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.group_item, parent, false);
        _binding.setLifecycleOwner(_lifecycleOwner);
        return new GroupsViewHolder(_binding , _groupsViewModel, _lifecycleOwner);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupsViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    // endregion

    // region ViewHolder

    public static class GroupsViewHolder extends RecyclerView.ViewHolder {

        private final GroupItemBinding _binding;
        private final GroupsViewModel _groupViewModel;
        private LifecycleOwner _lifeCycleOwner;

        public GroupsViewHolder(@NonNull GroupItemBinding binding, GroupsViewModel groupsViewModel, LifecycleOwner lifecycleOwner) {
            super(binding.getRoot());
            _binding = binding;
            _lifeCycleOwner = lifecycleOwner;
            _groupViewModel = groupsViewModel;
        }

        public void bind(Group group){
            _binding.setGroup(group);
            _binding.setGroupViewModel(_groupViewModel);

            _binding.groupLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public boolean onLongClick(View v) {
                    _groupViewModel.set_selectedGroupId(group.get_groupId());
                    _groupViewModel.setSelectedGroupIdForDb(_binding.getGroup().get_groupId());
                    return true;
                }
            });

            _groupViewModel.get_selectedGroupId().observe(_lifeCycleOwner, new Observer<String>() {
                        @Override
                        public void onChanged(String id) {
                            _binding.setSelectedGroupId(id);
                        }
            });
            _binding.setSelectedGroupId(_groupViewModel.get_selectedGroupId().getValue());
            _binding.executePendingBindings();
        }
    }

    // endregion
}
