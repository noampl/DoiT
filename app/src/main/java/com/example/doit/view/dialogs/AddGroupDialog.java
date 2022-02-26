package com.example.doit.view.dialogs;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.doit.R;
import com.example.doit.databinding.DialogFragmentAddGroupBinding;
import com.example.doit.interfaces.IDialogNavigationHelper;
import com.example.doit.model.entities.User;
import com.example.doit.viewmodel.GroupsViewModel;
import com.squareup.picasso.Picasso;

public class AddGroupDialog extends DialogFragment implements IDialogNavigationHelper {

    // region Members

    private DialogFragmentAddGroupBinding _binding;
    private AlertDialog _dialog;
    private GroupsViewModel _groupsViewModel;
    private Uri _imgUri;


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
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_add_group, container, false);
        _groupsViewModel = new ViewModelProvider(this).get(GroupsViewModel.class);
        _groupsViewModel.set_isBottomNavigationUp(true);
        _groupsViewModel.set_iDialogNavigationHelper(this);
        _binding.setGroupsViewModel(_groupsViewModel);
        initListeners();
        _binding.setLifecycleOwner(this);
        _dialog.setView(_binding.getRoot());
        _dialog.setCancelable(false);

        return _binding.getRoot();
    }

    private void initListeners(){
        _binding.avatarImg.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhoto.setType("image/*");
                    pickPhotoResultLauncher.launch(pickPhoto);
                }
        });

        _binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _groupsViewModel.clearNewGroup();
                dismiss();
            }
        });

        _binding.create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_groupsViewModel.addNewGroup(_imgUri,_binding.groupName.getText().toString(),
                        _binding.groupDec.getText().toString())) {

                    dismiss();
                }
                else {
                    Toast.makeText(getContext(),"Must enter Group Name",Toast.LENGTH_LONG).show();
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
        AddGroupDialogDirections.ActionAddGroupDialogToAdditionDialog action =
                AddGroupDialogDirections.actionAddGroupDialogToAdditionDialog();
        action.setMultipleChoice(true);
        Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(action);
    }
}
