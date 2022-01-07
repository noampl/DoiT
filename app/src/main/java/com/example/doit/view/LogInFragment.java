package com.example.doit.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


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

    public LogInFragment() {
        // Required empty public constructor
    }

    public static LogInFragment newInstance() {
        return new LogInFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_log_in, container, false);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        _binding.setLoginViewModel(viewModel);
        _binding.setLifecycleOwner(this);
        viewModel.set_isBottomNavUp(false);
        viewModel.get_logedIn().observe(getViewLifecycleOwner(),onUserAuthentication());
        _binding.registerButton.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_logInFragment2_to_registerFragment2));
        return _binding.getRoot();
    }

    private Observer<Boolean> onUserAuthentication() {
        return new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean != null) {
                    if(aBoolean) {
                        User user = viewModel.get_authUser().getValue();
                        Log.d(TAG, "User has been found");
                        Toast.makeText(getContext(), viewModel.getUserName() + " is connected", Toast.LENGTH_SHORT).show();
                        saveUserForLater();
                        Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(
                                R.id.action_logInFragment2_to_groupsFragment2);
                    } else {
                        Log.d(TAG, "User is not found");
                        Toast.makeText(getContext(), "User is not connected", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
    }

    private void saveUserForLater(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.email), viewModel.getUserName());
        editor.putString(getString(R.string.password), viewModel.getPassword()); // TODO figure out if needed to hash?
        editor.apply();
    }
}