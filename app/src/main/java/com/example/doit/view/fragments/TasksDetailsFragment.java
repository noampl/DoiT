package com.example.doit.view.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doit.R;
import com.example.doit.databinding.FragmentTasksDetailsBinding;
import com.example.doit.model.entities.Task;
import com.example.doit.viewmodel.TasksViewModel;


public class TasksDetailsFragment extends Fragment {

    // region Members

    private FragmentTasksDetailsBinding _binding;
    private TasksViewModel _taskViewModel;

    // endregion

    public TasksDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks_details, container, false);
        _taskViewModel = new ViewModelProvider(this).get(TasksViewModel.class);
        Task task = _taskViewModel.getTaskById();
        _binding.setTask(task);
        _binding.setAssignee(_taskViewModel.getUserById(task.get_assigneeId()));
        _binding.setOpenBy(_taskViewModel.getUserById(task.get_createdById()));
        _binding.setLifecycleOwner(this);
        // Inflate the layout for this fragment
        return _binding.getRoot();
    }
}