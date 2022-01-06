package com.example.doit.view;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.doit.R;
import com.example.doit.databinding.DialogFragmentAddGroupBinding;
import com.example.doit.generated.callback.OnClickListener;
import com.example.doit.viewmodel.GroupsViewModel;
import com.squareup.picasso.Picasso;

public class AddGroupDialog extends DialogFragment {

    // region Members

    private DialogFragmentAddGroupBinding _binding;
    private GroupsViewModel _groupsViewModel;

    // endregion

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_add_group, container, false);
        _groupsViewModel = new ViewModelProvider(this).get(GroupsViewModel.class);
        _groupsViewModel.set_isBottomNavigationUp(true);
        _binding.setGroupsViewModel(_groupsViewModel);
        initListeners();
        _binding.setLifecycleOwner(this);

        return _binding.getRoot();
    }

    private void initListeners(){
        _binding.avatarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhoto.setType("image/*");
                    pickPhotoResultLauncher.launch(pickPhoto);
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
                        Uri uri = data != null ? data.getData() : null;
                        Picasso.with(_binding.getRoot().getContext()).load(uri).fit().into(_binding.avatarImg);
                        _groupsViewModel.setImageUri(uri);
                    }
                }
            }
    );

}
