package com.example.doit.view.adapters;

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
import com.example.doit.databinding.MyTasksItemBinding;
import com.example.doit.model.entities.Group;
import com.example.doit.model.entities.Task;
import com.example.doit.model.entities.User;
import com.example.doit.viewmodel.TasksViewModel;

public class TasksAdapter extends ListAdapter<Task, TasksAdapter.TaskViewHolder> {

    // region Members

    private TasksViewModel _tasksViewModel;
    private boolean _isMyTasksScreen;
    private LifecycleOwner _lifeCycleOwner;

    // endregion

    // region C'tor

    public TasksAdapter(boolean isMyTasksScreen, LifecycleOwner lifecycleOwner) {
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
        set_lifeCycleOwner(lifecycleOwner);
    }

    // endregion

    // region Properties

    public void set_tasksViewModel(TasksViewModel _tasksViewModel) {
        this._tasksViewModel = _tasksViewModel;
    }

    public void set_isMyTasksScreen(boolean _isMyTasksScreen) {
        this._isMyTasksScreen = _isMyTasksScreen;
    }

    public void set_lifeCycleOwner(LifecycleOwner _lifeCycleOwner) {
        this._lifeCycleOwner = _lifeCycleOwner;
    }

    // endregion

    // region RecyclerView.Adapter

    @NonNull
    @Override
    public TasksAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyTasksItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.my_tasks_item, parent, false);
        return new TasksAdapter.TaskViewHolder(binding, _lifeCycleOwner);
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
        private LifecycleOwner _lifeCycleOwner;

        public TaskViewHolder(@NonNull MyTasksItemBinding binding , LifecycleOwner lifecycleOwner) {
            super(binding.getRoot());
            _binding = binding;
            _lifeCycleOwner = lifecycleOwner;
        }

        public void bind(Task task, Group group, User user, boolean _isMyTasksScreen, TasksViewModel tasksViewModel){
            _binding.setTask(task);
            _binding.setGroup(group);
            _binding.setUser(user);
            _binding.constraint.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    tasksViewModel.set_selectedTaskIndex(getAdapterPosition());
                    return true;
                }
            });

            tasksViewModel.get_selectedTaskIndex().observe(_lifeCycleOwner, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    _binding.setSelectedTask(integer);
                }
            });
            _binding.setItemPosition(getAdapterPosition());
            _binding.setIsMyTasks(_isMyTasksScreen);
            _binding.setTaskViewModel(tasksViewModel);
            _binding.executePendingBindings();
        }
    }

    // endregion
}
