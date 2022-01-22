package com.example.doit.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

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
import com.example.doit.databinding.FragmentChosenGroupBinding;
import com.example.doit.interfaces.IDialogNavigationHelper;
import com.example.doit.interfaces.IFragmentNavigitionHelper;
import com.example.doit.model.entities.Group;
import com.example.doit.model.entities.Task;
import com.example.doit.view.adapters.TasksAdapter;
import com.example.doit.viewmodel.GroupsViewModel;
import com.example.doit.viewmodel.TasksViewModel;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class ChosenGroupFragment extends Fragment implements IDialogNavigationHelper, IFragmentNavigitionHelper {

    // region Members

    private FragmentChosenGroupBinding _binding;
    private TasksViewModel _tasksViewModel;

    // endregion

    // region C'tor

    public ChosenGroupFragment() {
        // Required empty public constructor
    }

    // endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String selectedId = ChosenGroupFragmentArgs.fromBundle(getArguments()).getGroupId();
        Log.d("FRISHMAN", "SELECTED ID: " + selectedId);
        _binding = DataBindingUtil.inflate(inflater,R.layout.fragment_chosen_group ,container, false);
        _binding.setGroup(initVM(selectedId));
        _binding.setTasksViewModel(_tasksViewModel);
        _binding.setLifecycleOwner(this);
        initListeners();
        return _binding.getRoot();
    }

    private void initListeners(){
        TasksAdapter adapter = new TasksAdapter(false);
        adapter.set_tasksViewModel(_tasksViewModel);
        _tasksViewModel.get_tasks().observe(requireActivity(), new Observer<List<Task>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<Task> tasks) {
                adapter.submitList(tasks.stream().filter((t)->t.get_groupId().equals(_tasksViewModel.get_groupId())).collect(Collectors.toList()));
            }
        });
        _binding.taskLst.setAdapter(adapter);
    }

    private Group initVM(String selectedGroupId){
        _tasksViewModel = new ViewModelProvider(this).get(TasksViewModel.class);
        _tasksViewModel.set_iDialogNavigationHelper(this);
        _tasksViewModel.set_groupId(selectedGroupId);
        _tasksViewModel.set_iFragmentNavigationHelper(this);
        GroupsViewModel groupsViewModel = new ViewModelProvider(this).get(GroupsViewModel.class);
        return groupsViewModel.getGroupById(selectedGroupId);
    }

    @Override
    public void openDialog() {
        ChosenGroupFragmentDirections.ActionChosenGroupFragmentToAddTaskDialog action =
                ChosenGroupFragmentDirections.actionChosenGroupFragmentToAddTaskDialog(_tasksViewModel.get_groupId());
        Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(
                action);
    }

    @Override
    public void openFragment() {
        Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(
                R.id.action_chosenGroupFragment_to_tasksDetails);
    }
}