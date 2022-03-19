package com.example.doit.view.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.MyApplication;
import com.example.doit.common.Consts;
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

    //endregion

    // region LifeCycle

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container,false);
        viewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        init();

        return _binding.getRoot();
    }

    // endregion

    // region Private Methods

    private void init(){
        initBinding();
        initObservers();
        initListeners();
        initMenu();
        viewModel.updateUserDetails();

    }

    private void initMenu() {
        viewModel.getActionBarHelper().get().setTitle("My Account");
        viewModel.getActionBarHelper().get().setMenu(R.menu.account_menu);
        viewModel.getActionBarHelper().get().setNavIcon(R.drawable.ic_baseline_arrow_back_24);
        viewModel.getActionBarHelper().get().setNavigationClickListener(null);
        viewModel.getActionBarHelper().get().setMenuClickListener(menuItemClickListener);
    }

    private void initListeners(){
        _binding.profileImage.setOnClickListener(profileImageListener);
        _binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (viewModel.saveDetails(_binding.EmailEditText.getText().toString(),
                        _binding.FirstNameEditText.getText().toString(), _binding.LastNameEditText.getText().toString())){
                    viewModel.setEditDetails(false);
               }
               else{
                   Toast.makeText(requireContext(), "Invalid input",Toast.LENGTH_SHORT).show();
               }
            }
        });
    }

    private void initObservers(){
        viewModel.get_authUser().observe(getViewLifecycleOwner(), authUserChange);
        viewModel.get_operationError().setValue(Consts.INVALID_STRING);
        viewModel.get_operationError().observe(getViewLifecycleOwner(),errorHandler());
        viewModel.getEditDetails().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isEdit) {
                if (isEdit){
                    _binding.firstNameTextSwitch.showNext();
                    _binding.EmailSwitcher.showNext();
                    _binding.LastNameSwitcher.showNext();
                }
                else if (_binding.firstNameTextSwitch.getNextView() == _binding.FirstNameTextView){
                    _binding.firstNameTextSwitch.showNext();
                    _binding.EmailSwitcher.showNext();
                    _binding.LastNameSwitcher.showNext();
                }
            }
        });
    }

    private void initBinding(){
        _binding.setAccountViewModel(viewModel);
        _binding.setLifecycleOwner(this);
    }

    private final ActivityResultLauncher<Intent> pickPhotoResultLauncher = registerForActivityResult(
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

    private void clearLoginUser(){
        SharedPreferences sharedPref = MyApplication.getAppContext().getSharedPreferences(getString(R.string.file_key),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear().apply();
        editor.putString(getString(R.string.email), "NONE");
        editor.putString(getString(R.string.password), "NONE");
        editor.apply();
    }

    // region Observers

    private Observer<String> errorHandler() {
        return new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(!Objects.equals(s, "") && !Objects.equals(s, Consts.INVALID_STRING)) {
                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                    viewModel.updateUserDetails();
                    viewModel.get_operationError().postValue(Consts.INVALID_STRING);
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

    // endregion

    //region listeners

    private final View.OnClickListener profileImageListener = new View.OnClickListener() {
        @SuppressLint("IntentReset")
        @Override
        public void onClick(View v) {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickPhoto.setType("image/*");
            pickPhotoResultLauncher.launch(pickPhoto);
        }
    };


    // endregion

    // endregion

    // region Listeners

    @SuppressLint("NonConstantResourceId")
    private final Toolbar.OnMenuItemClickListener menuItemClickListener = item -> {
        switch (item.getItemId()){

            case R.id.edit:
                viewModel.setEditDetails(!viewModel.getEditDetails().getValue());

                return true;
            case R.id.about:
                Navigation.findNavController(requireActivity(),R.id.fragmentContainerView).navigate(R.id.about);

                return true;
            case R.id.logout:
                clearLoginUser();
                viewModel.onClickLogoutButton();
                Navigation.findNavController(requireActivity(),R.id.fragmentContainerView).
                        navigate(R.id.action_accountFragment2_to_logInFragment2);

                return true;
            default:
                break;
        }
        return false;
    };

    //
}
