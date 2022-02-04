package com.example.doit.view.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doit.R;
import com.example.doit.databinding.FragmentInitBinding;
import com.example.doit.viewmodel.LoginViewModel;

import java.util.HashMap;
import java.util.Map;


public class InitFragment extends Fragment {

    private LoginViewModel _loginViewModel;
    private boolean _triedToConnect;
    private HashMap<String,String> credentials;


    public InitFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentInitBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_init ,container,false);
        _loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        _loginViewModel.get_logedIn().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                        Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                                .navigate(R.id.action_initFragment_to_groupsFragment2);
                }
                else {
                    Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                            .navigate(R.id.action_initFragment_to_logInFragment2);
                }
            }
        });
        _loginViewModel.login(getUserCredentials());
        return binding.getRoot();
    }

    private Map<String, String> getUserCredentials(){
        if(_triedToConnect){
            return credentials;
        }
        _triedToConnect = true;
        credentials = new HashMap<>();
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        credentials.put("Email",sharedPref.getString(getString(R.string.email), "NONE"));
        credentials.put("Password",sharedPref.getString(getString(R.string.password), "NONE"));
        saveUserForLater();
        return credentials;
    }

    private void saveUserForLater(){
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.email), getUserCredentials().get("Email"));
        editor.putString(getString(R.string.password), getUserCredentials().get("Password"));
        editor.apply();
    }
}