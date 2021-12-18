package com.example.doit.view;

import android.os.Bundle;

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
import com.example.doit.R;
import com.example.doit.viewmodel.RegisterViewModel;
import com.example.doit.databinding.FragmentRegisterBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment implements IResponseHelper {
    private FragmentRegisterBinding _binding;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        RegisterViewModel viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        viewModel.setResponseHelper(this);
        _binding.setRegisterViewModel(viewModel);
        _binding.setLifecycleOwner(this);
        return _binding.getRoot();
    }

    @Override
    public void actionFinished(boolean actionResult) {
        Log.d("RegisterFragment", "res is " + actionResult);
        if( actionResult == false){
            Toast.makeText(getContext(), "ERROR: User has not been created", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getContext(), "User has been created", Toast.LENGTH_SHORT).show();
        Navigation.findNavController(getActivity(), R.id.fragmentContainerView).navigate(
                R.id.action_registerFragment2_to_groupsFragment2);
    }
}