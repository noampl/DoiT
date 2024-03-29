package com.example.doit.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doit.R;
import com.example.doit.common.Consts;
import com.example.doit.databinding.FragmentChosenGroupBinding;
import com.example.doit.interfaces.IDialogNavigationHelper;
import com.example.doit.interfaces.IFragmentNavigitionHelper;
import com.example.doit.model.entities.Group;
import com.example.doit.model.entities.Task;
import com.example.doit.view.adapters.TasksAdapter;
import com.example.doit.viewmodel.GroupsViewModel;
import com.example.doit.viewmodel.TasksViewModel;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


public class ChosenGroupFragment extends Fragment implements IDialogNavigationHelper, IFragmentNavigitionHelper {

    // region Members

    private FragmentChosenGroupBinding _binding;
    private TasksViewModel _tasksViewModel;
    private Group _group;

    // endregion

    // region C'tor

    public ChosenGroupFragment() {
        // Required empty public constructor
    }

    // endregion

    // region LifeCycle

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String selectedId = ChosenGroupFragmentArgs.fromBundle(getArguments()).getGroupId();
        _binding = DataBindingUtil.inflate(inflater,R.layout.fragment_chosen_group ,container, false);
        initVM(selectedId).thenAccept((group -> {
            _group = group;
            _binding.setGroup(group);
            initToolBar();
        }));
        _binding.setTasksViewModel(_tasksViewModel);
        _binding.setLifecycleOwner(this);
        initSwiper();
        initListeners();
        return _binding.getRoot();
    }

    // endregion

    // region Private Method

    private void initListeners(){
        TasksAdapter adapter = new TasksAdapter(false, getViewLifecycleOwner());
        adapter.set_tasksViewModel(_tasksViewModel);
        _tasksViewModel.get_tasks().observe(requireActivity(), new Observer<List<Task>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<Task> tasks) {
                List<Task> tmp = tasks.stream().filter((t)->t.get_groupId().equals(_tasksViewModel.get_groupId()) &&
                        t.get_finishDate() == 0).collect(Collectors.toList());
                tmp.sort(new Comparator<Task>() {
                    @Override
                    public int compare(Task task, Task task2) {
                        return task2.get_value() - task.get_value();
                    }
                });
                adapter.submitList(tmp);
                adapter.notifyDataSetChanged();
                if (tmp.size() > 0) {
                    _binding.noTasksText.setVisibility(View.INVISIBLE);
                } else {
                    _binding.noTasksText.setVisibility(View.VISIBLE);
                }
            }
        });
        _binding.taskLst.setAdapter(adapter);
        _tasksViewModel.get_selectedTaskID().observe(getViewLifecycleOwner(),new Observer<String>() {
            @Override
            public void onChanged(String id) {
                if (!id.equals(Consts.INVALID_STRING)) {
                    _tasksViewModel.get_actionBarHelper().get().setTitle("");
                    _tasksViewModel.get_actionBarHelper().get().setMenu(R.menu.only_delete);
                    _tasksViewModel.get_actionBarHelper().get().setMenuClickListener(menuItemClickListener);
                    _tasksViewModel.get_actionBarHelper().get().setNavIcon(R.drawable.ic_baseline_arrow_back_24);
                }
                else {
                    if (_group != null){
                        _tasksViewModel.get_actionBarHelper().get().setTitle(_group.get_name());
                    }
                    _tasksViewModel.get_actionBarHelper().get().setMenu(R.menu.app_menu_edit);
                    _tasksViewModel.get_actionBarHelper().get().setNavIcon(R.drawable.ic_baseline_arrow_back_24);
                    _tasksViewModel.get_actionBarHelper().get().setMenuClickListener(menuItemClickListener);
                }
            }
        });
        _binding.summary.setOnClickListener((v)->{
            ChosenGroupFragmentDirections.ActionChosenGroupFragmentToSummaryDialog action =
                    ChosenGroupFragmentDirections.actionChosenGroupFragmentToSummaryDialog(_group.get_groupId());
                Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                        .navigate(action); });
    }

    private CompletableFuture<Group> initVM(String selectedGroupId)  {
        GroupsViewModel groupsViewModel = new ViewModelProvider(this).get(GroupsViewModel.class);
        CompletableFuture<Group> group = groupsViewModel.getGroupById(selectedGroupId);
        _tasksViewModel = new ViewModelProvider(this).get(TasksViewModel.class);
        _tasksViewModel.set_iDialogNavigationHelper(this);
        _tasksViewModel.set_groupId(selectedGroupId);
        _tasksViewModel.set_iFragmentNavigationHelper(this);
        return group;
    }

    private void initToolBar(){
        _tasksViewModel.get_actionBarHelper().get().setTitle(_group.get_name());
        _tasksViewModel.get_actionBarHelper().get().setNavIcon(R.drawable.ic_baseline_arrow_back_24);
    }

    private void openEditTask(boolean isEdit){
        ChosenGroupFragmentDirections.ActionChosenGroupFragmentToTasksDetails action =
                ChosenGroupFragmentDirections.actionChosenGroupFragmentToTasksDetails(_tasksViewModel.get_tasksDetailsId());
        action.setIsEdit(isEdit);
        Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(action);
    }

    private void initSwiper(){
        _tasksViewModel.getIsTaskLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                _binding.swiper.setRefreshing(aBoolean);
            }
        });
        _binding.swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                _tasksViewModel.fetchRemoteTasks(_group.get_groupId());
            }
        });
    }

    // endregion

    // region IDialogNavigationHelper

    @Override
    public void openDialog() {
        ChosenGroupFragmentDirections.ActionChosenGroupFragmentToAddTaskDialog action =
                ChosenGroupFragmentDirections.actionChosenGroupFragmentToAddTaskDialog(_tasksViewModel.get_groupId());
        Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(
                action);
    }

    // endregion

    // region IFragmentNavigitionHelper

    @Override
    public void openFragment() {
        openEditTask(false);
    }

    // endregion

    // region Toolbar.OnMenuItemClickListener

    @SuppressLint("NonConstantResourceId")
    private final Toolbar.OnMenuItemClickListener menuItemClickListener = item -> {
        switch (item.getItemId()){
            case R.id.delete:
                _tasksViewModel.deleteTask(_tasksViewModel.get_selectedTaskID().getValue());

                return true;
            case R.id.about:
                Navigation.findNavController(requireActivity(),R.id.fragmentContainerView).navigate(R.id.about);

                return true;
            case R.id.edit:
            ChosenGroupFragmentDirections.ActionChosenGroupFragmentToGroupsDetailsFragment action =
                    ChosenGroupFragmentDirections.actionChosenGroupFragmentToGroupsDetailsFragment(_group.get_groupId());
            Navigation.findNavController(requireActivity(),R.id.fragmentContainerView).navigate(action);

                return true;
            default:
                break;
        }
        return false;
    };

    // endregion
}