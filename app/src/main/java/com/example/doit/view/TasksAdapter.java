package com.example.doit.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doit.R;
import com.example.doit.databinding.TasksItemBinding;
import com.example.doit.model.entities.Task;

public class TasksAdapter extends ListAdapter<Task, TasksAdapter.TaskViewHolder> {

    // region C'tor

    protected TasksAdapter() {
        super(new DiffUtil.ItemCallback<Task>() {
            @Override
            public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
                return oldItem.get_taskId().equals(newItem.get_taskId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
                return oldItem.equals(newItem);
            }
        });
    }

    // endregion

    // region RecyclerView.Adapter

    @NonNull
    @Override
    public TasksAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TasksItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.tasks_item, parent, false);
        return new TasksAdapter.TaskViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksAdapter.TaskViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    // endregion

    // region ViewHolder

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        private final TasksItemBinding _binding;

        public TaskViewHolder(@NonNull TasksItemBinding binding) {
            super(binding.getRoot());
            _binding = binding;
        }

        public void bind(Task task){
            _binding.setTask(task);
            _binding.executePendingBindings();
        }
    }

    // endregion
}
