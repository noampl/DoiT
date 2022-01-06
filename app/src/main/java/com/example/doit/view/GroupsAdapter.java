package com.example.doit.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doit.databinding.GroupItemBinding;
import com.example.doit.model.entities.Group;
import com.example.doit.viewmodel.GroupsViewModel;

import java.util.List;

public class GroupsAdapter extends ListAdapter<Group, GroupsAdapter.GroupsViewHolder>{

    // region Members

    private GroupsViewModel _groupsViewModel;

    // endregion

    // region C'tor

    protected GroupsAdapter(GroupsViewModel groupsViewModel) {
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
    }

    // endregion

    // region Properties

    // endregion

    // region RecyclerAdapter

    @NonNull
    @Override
    public GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GroupItemBinding _binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), viewType, parent, false);
        return new GroupsViewHolder(_binding , _groupsViewModel);
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

        public GroupsViewHolder(@NonNull GroupItemBinding binding, GroupsViewModel groupsViewModel) {
            super(binding.getRoot());
            _binding = binding;
            _groupViewModel =groupsViewModel;
        }

        public void bind(Group group){
            _binding.setGroup(group);
            _binding.setGroupViewModel(_groupViewModel);
            _binding.executePendingBindings();
        }
    }

    // endregion
}
