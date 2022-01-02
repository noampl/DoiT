package com.example.doit.view;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doit.R;
import com.example.doit.databinding.FragmentAccountBinding;
import com.example.doit.viewmodel.AccountViewModel;
import com.squareup.picasso.Picasso;

public class AccountFragment extends Fragment {
    //region members
    private static final String TAG = "Account Fragment";
    private FragmentAccountBinding _binding;
    private AccountViewModel viewModel;
    //endregion

    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance() { return new AccountFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = DataBindingUtil.inflate(inflater,R.layout.fragment_account, container,false);
        viewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        _binding.setAccountViewModel(viewModel);
        _binding.setLifecycleOwner(this);
        viewModel.updateUserDetails();
        return _binding.getRoot();
    }

}