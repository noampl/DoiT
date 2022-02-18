package com.example.doit.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
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
import com.example.doit.interfaces.IFragmentNavigitionHelper;
import com.example.doit.model.entities.Task;
import com.example.doit.view.adapters.TasksAdapter;
import com.example.doit.viewmodel.TasksViewModel;

import java.util.List;
import java.util.stream.Collectors;


public class MyTasksFragment extends Fragment implements IFragmentNavigitionHelper {

    // region Members

    private static final String TAG = "MyTasksFragment";
    private TasksViewModel _tasksViewModel;
    private FragmentMyTasksBinding _binding;

    // endregion

    // region C'tor

    public MyTasksFragment() {
        // Required empty public constructor
    }

    // endregion

    // region LifeCycle

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

    // endregion

    // region Private Methods

    private void initListeners(){
        TasksAdapter adapter = new TasksAdapter(true, getViewLifecycleOwner());
        adapter.set_tasksViewModel(_tasksViewModel);
        _tasksViewModel.fetchTasks();
        _tasksViewModel.set_iFragmentNavigationHelper(this);
        adapter.submitList(_tasksViewModel.get_tasks().getValue().stream().filter(t->(t.get_finishDate() != 0)).collect(Collectors.toList()));
        _binding.taskLst.setAdapter(adapter);
        _tasksViewModel.get_tasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<Task> tasks) {
                adapter.submitList(tasks.stream().filter((t)->(t.get_finishDate() == 0)).collect(Collectors.toList()));
            }
        });
        menuChanger();
    }

    private void menuChanger(){
        _tasksViewModel.get_selectedTaskIndex().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer >= 0) {
                    _tasksViewModel.get_actionBarHelper().get().setTitle("");
                    _tasksViewModel.get_actionBarHelper().get().setMenu(R.menu.edit_menu);
                    _tasksViewModel.get_actionBarHelper().get().setMenuClickListener(menuItemClickListener);

                }
                else {
                    _tasksViewModel.get_actionBarHelper().get().setTitle("My Tasks");
                    _tasksViewModel.get_actionBarHelper().get().setMenu(R.menu.app_menu);
                    _tasksViewModel.get_actionBarHelper().get().setMenuClickListener(null);

                }
            }
        });
    }

    // endregion

    // region IFragmentNavigitionHelper

    @Override
    public void openFragment() {
        MyTasksFragmentDirections.ActionMyTasksFragmentToTasksDetails action =
                MyTasksFragmentDirections.actionMyTasksFragmentToTasksDetails(_tasksViewModel.get_tasksDetailsId());
        Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(action);
    }

    // endregion

    // region Toolbar.OnMenuItemClickListener

    @SuppressLint("NonConstantResourceId")
    private final Toolbar.OnMenuItemClickListener menuItemClickListener = item -> {
        switch (item.getItemId()) {
            case R.id.delete:
                Log.d("Peleg", "DELETE in MyTasks fragment");

                return true;
            case R.id.edit:

                Log.d("Peleg", "EDIT in MyTasks fragment");
                return true;
            default:

                break;
        }
        return false;
    };

    // endregion
}