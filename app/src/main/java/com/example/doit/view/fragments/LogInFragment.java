package com.example.doit.view.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.example.doit.databinding.FragmentLogInBinding;
import com.example.doit.viewmodel.LoginViewModel;

public class LogInFragment extends Fragment {

    //region members

    private static final String TAG = "Login Fragment";
    private FragmentLogInBinding _binding;
    private LoginViewModel viewModel;

    //endregion

    // region LifeCycle

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_log_in, container, false);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewModel.get_Error().setValue(Consts.INVALID_STRING);
        init();

        return _binding.getRoot();
    }

    // endregion

    // region Private Methods

    private void init(){
        viewModel.set_isBottomNavUp(false);
        initMenu();
        initBinding();
        initObservers();
        initListeners();
    }

    private void initListeners(){
        _binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(requireActivity(),R.id.fragmentContainerView).navigate(
                        R.id.action_logInFragment2_to_registerFragment2);
            }
        });
    }

    private void initObservers(){
        viewModel.get_logedIn().observe(getViewLifecycleOwner(),onUserAuthentication());
        viewModel.get_Error().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.length() > 0 && !s.equals(Consts.INVALID_STRING)){
                    Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
                    viewModel.get_Error().postValue(Consts.INVALID_STRING);
                }
            }
        });
    }

    private void initBinding(){
        _binding.setLoginViewModel(viewModel);
        _binding.setLifecycleOwner(this);
    }

    private void initMenu(){
        viewModel.get_actionBarHelper().get().setNavIcon(null);
        viewModel.get_actionBarHelper().get().setMenu(R.menu.app_menu);
        viewModel.get_actionBarHelper().get().setMenuClickListener(null);
        viewModel.get_actionBarHelper().get().setNavigationClickListener(null);
        viewModel.get_actionBarHelper().get().setTitle("Login");

    }

    private Observer<Boolean> onUserAuthentication() {
        return new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean != null) {
                    if(aBoolean) {
                        User user = viewModel.get_authUser().getValue();
                        if (viewModel.getUserName() != null){
                            Toast.makeText(getContext(), viewModel.getUserName() + " is connected", Toast.LENGTH_SHORT).show();
                        } else {
                            if (user != null) {
                                Toast.makeText(getContext(), user.get_email() + " is connected", Toast.LENGTH_SHORT).show();
                            }
                        }
                        saveUserForLater();
                        Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(
                                R.id.action_logInFragment2_to_groupsFragment2);
                    } else {
                       if(viewModel.get_Error() != null && !viewModel.get_Error().getValue().equals(Consts.INVALID_STRING)){
                           Toast.makeText(getContext(), viewModel.get_Error().getValue(), Toast.LENGTH_SHORT).show();
                       }
                    }
                }
            }
        };
    }

    private void saveUserForLater(){
        if(viewModel.getUserName() != "" && viewModel.getUserName() != null){
            SharedPreferences sharedPref =  MyApplication.getAppContext().getSharedPreferences(getString(R.string.file_key),Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear().apply();
            editor.putString(getString(R.string.email), viewModel.getUserName());
            editor.putString(getString(R.string.password), viewModel.getPassword()); // TODO figure out if needed to hash?
            editor.apply();
        }
    }

    // endregion

}