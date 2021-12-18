package com.example.doit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.doit.databinding.FragmentLogInBinding;

import java.util.Objects;

public class LogInFragment extends Fragment implements IResponseHelper {
    //region members
    private static final String TAG = "Login Fragment";
    private FragmentLogInBinding _binding;
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
        LoginViewModel viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        _binding.setLoginViewModel(viewModel);
        _binding.setLifecycleOwner(this);
        viewModel.setResponseHelper(this);
        _binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container_view, RegisterFragment.class, null)
                        .commit();
            }
        });
        return _binding.getRoot();
    }

    @Override
    public void actionFinished(boolean actionResult) {
        if (actionResult){
            Log.d(TAG, "User is find");
            Toast.makeText(getContext(), "User is connected", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "User is not find");
            Toast.makeText(getContext(), "User is not connected", Toast.LENGTH_SHORT).show();
        }
    }
}