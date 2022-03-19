package com.example.doit.view.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.MyApplication;
import com.example.doit.common.Consts;
import com.example.doit.model.entities.User;
import com.example.doit.R;
import com.example.doit.databinding.FragmentRegisterBinding;
import com.example.doit.viewmodel.RegisterViewModel;
import com.squareup.picasso.Picasso;

public class RegisterFragment extends Fragment{

    // region Members

    private FragmentRegisterBinding _binding;
    private RegisterViewModel viewModel;

    // endregion

    // region LifeCycle

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        viewModel.setRegisterError(Consts.INVALID_STRING);
        init();
        return _binding.getRoot();
    }

    // endregion

    // region Private Methods

    private void init(){
        initObservers();
        initBinding();
        initListeners();
        initMenu();
        Picasso.with(_binding.getRoot().getContext()).load(R.drawable.no_profile_picture).fit()
                .into(_binding.profileImageButton);
    }

    private void initObservers(){
        viewModel.get_authUser().observe(getViewLifecycleOwner(), loggedInObserver());
        viewModel.getPasswordsIdentical().observe(getViewLifecycleOwner(), passwordIdenticalObserver());
        viewModel.getRegisterError().observe(getViewLifecycleOwner(), errorObserver());
    }

    private void initBinding(){
        _binding.setRegisterViewModel(viewModel);
        _binding.setLifecycleOwner(this);
    }

    private void initListeners(){
        _binding.profileImageButton.setOnClickListener(replaceImage());
    }

    private void initMenu(){
        viewModel.getActionBarHelper().get().setTitle("Register");
        viewModel.getActionBarHelper().get().setMenuClickListener(null);
        viewModel.getActionBarHelper().get().setMenu(R.menu.app_menu);
        viewModel.getActionBarHelper().get().setNavigationClickListener(null);

    }

    private Observer<Boolean> passwordIdenticalObserver(){
        return new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean){
                    _binding.validatPassword.setError("Passwords is not identical");
                } else {
                    _binding.validatPassword.setError(null,null);
                }
            }
        };
    }

    private Observer<String> errorObserver(){
        return new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s != null && !s.equals(Consts.INVALID_STRING)){
                    Toast.makeText(getContext(), s,Toast.LENGTH_SHORT).show();
                    viewModel.setRegisterError(Consts.INVALID_STRING);
                }
            }
        };
    }

    private Observer<User> loggedInObserver(){
        return new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user.get_userId() != null && user.get_userId().length() > 5) {
                    Toast.makeText(getContext(), "hello " + user.get_firstName(), Toast.LENGTH_SHORT).show();
                    saveUserForLater();
                    Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(
                            R.id.action_registerFragment2_to_groupsFragment2);
                }
            }
        };
    }

    private final ActivityResultLauncher<Intent> pickPhotoResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Uri uri = data != null ? data.getData() : null;
                        Picasso.with(_binding.getRoot().getContext()).load(uri).fit().into(_binding.profileImageButton);
                        viewModel.setImageUri(uri);
                    }
                }
            }
    );

    private View.OnClickListener replaceImage() {
        return new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickPhoto.setType("image/*");
                pickPhotoResultLauncher.launch(pickPhoto);
            }
        };
    }

    private void saveUserForLater(){
        SharedPreferences sharedPref =  MyApplication.getAppContext().getSharedPreferences(getString(R.string.file_key),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.email), viewModel.get_email().getValue());
        editor.putString(getString(R.string.password), viewModel.get_password().getValue());
        editor.apply();
    }

    // endregion
}