package com.example.doit.view.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.doit.model.entities.User;
import com.example.doit.R;
import com.example.doit.databinding.FragmentAccountBinding;
import com.example.doit.viewmodel.AccountViewModel;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class AccountFragment extends Fragment {

    //region members

    private static final String TAG = "Account Fragment";
    private FragmentAccountBinding _binding;
    private AccountViewModel viewModel;
    private boolean editDetails;
    private boolean image_changed;

    //endregion

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container,false);
        viewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        _binding.setAccountViewModel(viewModel);
        _binding.setLifecycleOwner(this);
        editDetails = false;
        _binding.userEditButton.setOnClickListener(userEditButtonListener);
        _binding.profileImage.setOnClickListener(profileImageListener);
        _binding.profileImage.setClickable(false);
        _binding.profileImage.setEnabled(false);
        viewModel.updateUserDetails();
        viewModel.get_authUser().observe(getViewLifecycleOwner(), authUserChange);
        viewModel.get_operationError().observe(getViewLifecycleOwner(),errorHandler());
        return _binding.getRoot();
    }

    ActivityResultLauncher<Intent> pickPhotoResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Uri uri = data != null ? data.getData() : null;
                        Picasso.with(_binding.getRoot().getContext()).load(uri).fit().into(_binding.profileImage);
                        assert uri != null;
                        viewModel.setImageUrl(uri.toString());
                        viewModel.setImageChanged(true);
                    }
                }
            }
    );

    private Observer<String> errorHandler() {
        return new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(!Objects.equals(s, "")) {
                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                    viewModel.updateUserDetails();
                    viewModel.get_operationError().postValue("");
                }
            }
        };
    }

    private final Observer<User> authUserChange = new Observer<User>() {
        @Override
        public void onChanged(User user) {
            if (user.get_userId() == null && Boolean.FALSE.equals(viewModel.get_authSuccess().getValue())) {
                Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(
                        R.id.action_accountFragment2_to_logInFragment2);
                return;
            }
            viewModel.updateUserDetails();
        }
    };

    //region listeners
    View.OnClickListener profileImageListener = new View.OnClickListener() {
        @SuppressLint("IntentReset")
        @Override
        public void onClick(View v) {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickPhoto.setType("image/*");
            pickPhotoResultLauncher.launch(pickPhoto);
        }
    };

    View.OnClickListener userEditButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Boolean editDetails = viewModel.getEditDetails().getValue();
            if(Boolean.FALSE.equals(editDetails)){
                viewModel.setImageChanged(false);
                viewModel.setEditDetails(true);
            } else {
                viewModel.setEditDetails(false);
                viewModel.changeDetails();
            }
        }
    };
}
    // endregion
