package com.example.doit.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doit.R;
import com.example.doit.databinding.FragmentMyTasksBinding;
import com.example.doit.interfaces.IDialogNavigationHelper;
import com.example.doit.model.Repository;
import com.example.doit.model.entities.Task;
import com.example.doit.view.adapters.TasksAdapter;
import com.example.doit.viewmodel.TasksViewModel;

import java.util.List;


public class MyTasksFragment extends Fragment {

    // region Members

    private TasksViewModel _tasksViewModel;
    private FragmentMyTasksBinding _binding;

    // endregion

    public MyTasksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _tasksViewModel = new ViewModelProvider(this).get(TasksViewModel.class);
        _tasksViewModel.fetchTasks();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_tasks,container,false);
        initListeners();
        _binding.setTaskViewModel(_tasksViewModel);
        _binding.setLifecycleOwner(this);
        return _binding.getRoot();
    }

    // region Private Methods

    private void initListeners(){
        TasksAdapter adapter = new TasksAdapter(true);
        adapter.set_tasksViewModel(_tasksViewModel);
        _tasksViewModel.fetchTasks();
        adapter.submitList(_tasksViewModel.get_tasks().getValue());
        _binding.taskLst.setAdapter(adapter);
        _tasksViewModel.get_tasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<Task> tasks) {
                Log.d("PELEG", "submit tasks size " + tasks.size());
                adapter.submitList(tasks);
                adapter.notifyDataSetChanged();;

                Log.d("PELEG", "submit tasks to myTasks");
            }
        });
    }

    // endregion
}