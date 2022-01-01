package com.example.doit.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.doit.IResponseHelper;
import com.example.doit.R;
import com.example.doit.viewmodel.RegisterViewModel;
import com.example.doit.databinding.FragmentRegisterBinding;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment implements IResponseHelper {
    private FragmentRegisterBinding _binding;
    private RegisterViewModel viewModel;

    ActivityResultLauncher<Intent> pickPhotoResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Uri uri = data != null ? data.getData() : null;
                        _binding.profileImageButton.setImageURI(uri);
                        viewModel.get_image_uri().setValue(uri);
                    }
                }
            }
    );

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        viewModel.setResponseHelper(this);
        _binding.setRegisterViewModel(viewModel);
        _binding.setLifecycleOwner(this);
        _binding.progressBar.setVisibility(View.GONE);
        viewModel.get_registering_job_run().observe(getViewLifecycleOwner(), runningJobObserver);
        _binding.profileImageButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickPhoto.setType("image/*");
                pickPhotoResultLauncher.launch(pickPhoto);
            }
        });

        return _binding.getRoot();
    }

    final Observer<Boolean> runningJobObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean aBoolean) {
            if (!aBoolean) {
                _binding.progressBar.setVisibility(View.GONE);
            } else {
                _binding.progressBar.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void actionFinished(boolean actionResult) {
        _binding.progressBar.setVisibility(View.GONE);
        Log.d("RegisterFragment", "res is " + actionResult);
        if(!actionResult){
            Toast.makeText(getContext(), viewModel.getErrorReason(), Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getContext(), "User has been created", Toast.LENGTH_SHORT).show();
        Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(
                R.id.action_registerFragment2_to_groupsFragment2);
    }
};