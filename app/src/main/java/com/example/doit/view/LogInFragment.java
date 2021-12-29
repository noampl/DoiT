package com.example.doit.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.doit.IResponseHelper;
import com.example.doit.viewmodel.LoginViewModel;
import com.example.doit.R;
import com.example.doit.databinding.FragmentLogInBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LogInFragment extends Fragment implements IResponseHelper {
    //region members
    private static final String TAG = "Login Fragment";
    private FragmentLogInBinding _binding;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    //endregion

    public LogInFragment() {
        // Required empty public constructor
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
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
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
        LoginViewModel viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        _binding.setLoginViewModel(viewModel);
        _binding.setLifecycleOwner(this);
        viewModel.setResponseHelper(this);
        _binding.registerButton.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_logInFragment2_to_registerFragment2));
        return _binding.getRoot();
    }

    @Override
    public void actionFinished(boolean actionResult) {
        if (actionResult){
            Log.d(TAG, "User is find");
            Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigate(
                    R.id.action_logInFragment2_to_groupsFragment2);
            String email = user.getEmail();
            Toast.makeText(getContext(), email + " is connected", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "User is not find");
            Toast.makeText(getContext(), "User is not connected", Toast.LENGTH_SHORT).show();
        }
    }
}