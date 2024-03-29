package com.example.doit.view.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.doit.R;
import com.example.doit.databinding.DialogFragmentAddTaskBinding;
import com.example.doit.interfaces.IDialogNavigationHelper;
import com.example.doit.viewmodel.TasksViewModel;
import com.google.android.material.datepicker.MaterialStyledDatePickerDialog;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

public class AddTaskDialog extends DialogFragment implements IDialogNavigationHelper {

    // region Members

    private DialogFragmentAddTaskBinding _binding;
    private AlertDialog _dialog;
    private TasksViewModel tasksViewModel;
    private Uri _imgUri;
    private String _groupId;

    // endregion

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        _dialog = builder.create();
        return _dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _groupId = AddTaskDialogArgs.fromBundle(getArguments()).getGroupId();
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_add_task, container, false);
        tasksViewModel = new ViewModelProvider(this).get(TasksViewModel.class);
        tasksViewModel.set_iDialogNavigationHelper(this);
        tasksViewModel.set_groupId(_groupId);
        _binding.setTaskViewModel(tasksViewModel);
        initListeners();
        _binding.setLifecycleOwner(this);
        _dialog.setView(_binding.getRoot());
        _dialog.setCancelable(false);
        return _binding.getRoot();
    }

    private void initListeners(){
        _binding.valueSpinner.setAdapter( ArrayAdapter.createFromResource(requireContext(),
                R.array.values, R.layout.value_spinner));

        _binding.avatarImg.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickPhoto.setType("image/*");
                pickPhotoResultLauncher.launch(pickPhoto);
            }
        });

        _binding.datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = new Calendar.Builder().setInstant(new Date()).build();
                @SuppressLint("RestrictedApi") MaterialStyledDatePickerDialog dialog =
                        new MaterialStyledDatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                tasksViewModel.setTargetDate(datePicker);

                            }
                        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        _binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        _binding.create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tasksViewModel.createTask(_imgUri,_binding.taskName.getText().toString(),
                        _binding.taskDec.getText().toString(),
                        Integer.parseInt(_binding.valueSpinner.getSelectedItem().toString()), _groupId)){
                    dismiss();
                }
                else{
                    Toast.makeText(requireContext(),"Please enter task name",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    ActivityResultLauncher<Intent> pickPhotoResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        _imgUri = data != null ? data.getData() : null;
                        Picasso.with(_binding.getRoot().getContext()).load(_imgUri).fit().into(_binding.avatarImg);
                    }
                }
            }
    );

    @Override
    public void openDialog() {
        AddTaskDialogDirections.ActionAddTaskDialogToAdditionDialog action =
        AddTaskDialogDirections.actionAddTaskDialogToAdditionDialog();
        action.setGroupId(_groupId);
        action.setIsRemoteSearcher(false);
        Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(action);
    }
}
