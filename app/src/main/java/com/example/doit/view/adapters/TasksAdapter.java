package com.example.doit.view.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doit.R;
import com.example.doit.databinding.MyTasksItemBinding;
import com.example.doit.model.entities.Group;
import com.example.doit.model.entities.Task;
import com.example.doit.model.entities.User;
import com.example.doit.viewmodel.TasksViewModel;

public class TasksAdapter extends ListAdapter<Task, TasksAdapter.TaskViewHolder> {

    // region Members

    private TasksViewModel _tasksViewModel;
    private boolean _isMyTasksScreen;

    // endregion

    // region C'tor

    public TasksAdapter( boolean isMyTasksScreen) {
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
        set_isMyTasksScreen(isMyTasksScreen);
    }

    // endregion

    // region Properties

    public void set_tasksViewModel(TasksViewModel _tasksViewModel) {
        this._tasksViewModel = _tasksViewModel;
    }

    public void set_isMyTasksScreen(boolean _isMyTasksScreen) {
        this._isMyTasksScreen = _isMyTasksScreen;
    }

    // endregion

    // region RecyclerView.Adapter

    @NonNull
    @Override
    public TasksAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyTasksItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.my_tasks_item, parent, false);
        return new TasksAdapter.TaskViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksAdapter.TaskViewHolder holder, int position) {
        if(getItemCount() > 0){
            User user =_tasksViewModel.getUserByTask(getItem(position));
            Group group = _tasksViewModel.getGroupByTask(getItem(position));
            holder.bind(getItem(position), group, user, _isMyTasksScreen, _tasksViewModel);
        }
    }

    // endregion

    // region ViewHolder

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        private final MyTasksItemBinding _binding;

        public TaskViewHolder(@NonNull MyTasksItemBinding binding) {
            super(binding.getRoot());
            _binding = binding;
        }

        public void bind(Task task, Group group, User user, boolean _isMyTasksScreen, TasksViewModel tasksViewModel){
            _binding.setTask(task);
            _binding.setGroup(group);
            _binding.setUser(user);
            _binding.setIsMyTasks(_isMyTasksScreen);
            _binding.setPosition(getAdapterPosition());
            _binding.executePendingBindings();
        }
    }

    // endregion
}
