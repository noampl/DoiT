package com.example.doit.view.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;

import com.example.doit.R;
import com.example.doit.databinding.FragmentTasksDetailsBinding;
import com.example.doit.model.entities.Task;
import com.example.doit.view.adapters.UsersAdapter;
import com.example.doit.viewmodel.TasksViewModel;
import com.example.doit.viewmodel.UsersViewModel;
import com.google.android.material.datepicker.MaterialStyledDatePickerDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class TasksDetailsFragment extends Fragment {

    // region Members

    private FragmentTasksDetailsBinding _binding;
    private TasksViewModel _taskViewModel;
    private UsersViewModel _usersViewModel;
    private Task _task;
    private String _prevAssigneeId;

    // endregion

    // region LifeCycle

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String taskId = TasksDetailsFragmentArgs.fromBundle(getArguments()).getTaskId();
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks_details, container, false);
        _taskViewModel = new ViewModelProvider(this).get(TasksViewModel.class);
        _usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        _taskViewModel.set_isEdit(TasksDetailsFragmentArgs.fromBundle(getArguments()).getIsEdit());
        _taskViewModel.getTaskById(taskId).thenAccept(this::onFutureComplete);
        init();

        // Inflate the layout for this fragment
        return _binding.getRoot();
    }

    // endregion

    // region Private Methods

    private void init(){
        initTollBar();
        initListeners();
        initObservers();
        initBinding();
    }

    private void initBinding(){
        _binding.setTaskViewModel(_taskViewModel);
        _binding.valueSpinner.setAdapter(ArrayAdapter.createFromResource(requireContext(),
                R.array.values, R.layout.value_spinner));
        _binding.setLifecycleOwner(this);
        _binding.executePendingBindings();
    }

    private void initTollBar(){
        _taskViewModel.get_actionBarHelper().get().setMenu(R.menu.edit_menu);
        _taskViewModel.get_actionBarHelper().get().setTitle("Details");
        _taskViewModel.get_actionBarHelper().get().setMenuClickListener(menuItemClickListener);
    }

    @SuppressLint("IntentReset")
    private void initListeners(){
        _binding.taskImg.setOnClickListener(view -> {
            if (_taskViewModel.get_isEdit().getValue()){
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickPhoto.setType("image/*");
                pickPhotoResultLauncher.launch(pickPhoto);
            }
        });

        _binding.finishAtVal.setOnClickListener(view -> {
            if (_taskViewModel.get_isEdit().getValue()){
                Calendar calendar = new Calendar.Builder().setInstant(new Date()).build();
                @SuppressLint("RestrictedApi") MaterialStyledDatePickerDialog dialog =
                        new MaterialStyledDatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                _taskViewModel.setTargetDate(datePicker);

                            }
                        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        _binding.valueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                _task.set_value(Integer.parseInt(adapterView.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        _binding.save.setOnClickListener(view -> {
                _taskViewModel.saveChanges(_task, _binding.taskNameEdit.getText().toString(),
                        _binding.taskDescEdit.getText().toString(), _binding.checkbox.isChecked());
                _binding.setTask(_task);
                _usersViewModel.set_selectedUser(new ArrayList<>());
        });

        _binding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                _taskViewModel.setTaskChecked(_task, b, _binding.getIsEdit());
            }
        });

        _binding.assigneeBtn.setOnClickListener((v)->selectAssignee());
    }

    private void initObservers() {
        _taskViewModel.get_isEdit().observe(getViewLifecycleOwner(), isEdit -> {
            if (isEdit){
                _binding.taskDescSwitcher.showNext();
                _binding.taskNameSwitcher.showNext();
                _binding.valueNumberSwitcher.showNext();
                _binding.assigneeAvatarSwitcher.showNext();
            }
            else if (_binding.taskDescSwitcher.getNextView() == _binding.taskDescText){
                _binding.taskDescSwitcher.showNext();
                _binding.taskNameSwitcher.showNext();
                _binding.valueNumberSwitcher.showNext();
                _binding.assigneeAvatarSwitcher.showNext();
            }
            _binding.setIsEdit(isEdit);
        });

        _usersViewModel.get_selectedUser().observe(getViewLifecycleOwner(), users ->{
                if (users!= null && users.size() > 0) {
                    _binding.setAssignee(users.get(0));
                    _task.set_assigneeId(users.get(0).get_userId());
                    _binding.executePendingBindings();
                }
                else {
                    if (_prevAssigneeId != null)
                        _task.set_assigneeId(_prevAssigneeId);
                }
        });

        _taskViewModel.get_targetDate().observe(getViewLifecycleOwner(), (l)-> {
            if (_task != null) {
                _task.set_targetDate(l);
                _binding.setTargetDate(l);
                _binding.executePendingBindings();
            }
        });

    }

    private void onFutureComplete(Task task){
        _task = task;
        _prevAssigneeId = task.get_assigneeId();
        _usersViewModel.setUsersById(task.get_groupId());
        _binding.setTask(task);
        _binding.setAssignee(_taskViewModel.getUserById(task.get_assigneeId()));
        _binding.setOpenBy(_taskViewModel.getUserById(task.get_createdById()));
        _taskViewModel.get_targetDate().postValue(task.get_targetDate());
    }

    private void selectAssignee(){
        TasksDetailsFragmentDirections.ActionTasksDetailsToAdditionDialog action =
                TasksDetailsFragmentDirections.actionTasksDetailsToAdditionDialog();
        action.setGroupId(_task.get_groupId());
        action.setIsChecked(false);
        Navigation.findNavController(requireActivity(),R.id.fragmentContainerView).navigate(action);
    }

    // endregion

    // region Toolbar.OnMenuItemClickListener

    @SuppressLint("NonConstantResourceId")
    private final Toolbar.OnMenuItemClickListener menuItemClickListener = item -> {
        switch (item.getItemId()) {
            case R.id.delete:
                _taskViewModel.deleteTask(_task);
                Navigation.findNavController(requireActivity(),R.id.fragmentContainerView).navigateUp();
                return true;
            case R.id.edit:
                _taskViewModel.set_isEdit(!_taskViewModel.get_isEdit().getValue());

                return true;
            default:

                break;
        }
        return false;
    };

    // endregion

    // region ActivityResultLauncher

    ActivityResultLauncher<Intent> pickPhotoResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        _task.set_image(data != null ? data.getData().toString() : null);
                        Picasso.with(_binding.getRoot().getContext()).load(_task.get_image()).fit().into(_binding.taskImg);
                    }
                }
            }
    );

    // endregion
}