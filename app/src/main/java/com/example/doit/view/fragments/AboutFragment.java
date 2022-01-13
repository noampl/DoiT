package com.example.doit.view.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.doit.R;
import com.example.doit.databinding.FragmentAboutBinding;
import com.example.doit.databinding.FragmentAccountBinding;
import com.example.doit.model.entities.User;
import com.example.doit.viewmodel.AccountViewModel;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class AboutFragment extends Fragment {
    //region members
    private static final String TAG = "About Fragment";
    private FragmentAboutBinding _binding;
    //endregion

    public AboutFragment() {
        // Required empty public constructor
    }

    public static AboutFragment newInstance() { return new AboutFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, container,false);
        _binding.setLifecycleOwner(this);
        return _binding.getRoot();
    }

}
    // endregion
