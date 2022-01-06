package com.example.doit.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.doit.R;
import com.example.doit.databinding.DialogFragmentAddGroupBinding;
import com.example.doit.viewmodel.GroupsViewModel;

public class AddGroupDialog extends DialogFragment {

    // region Members

    private DialogFragmentAddGroupBinding _binding;
    private GroupsViewModel _groupsViewModel;

    // endregion

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_add_group, container, false);
        _groupsViewModel = new ViewModelProvider(this).get(GroupsViewModel.class);
        _groupsViewModel.set_isBottomNavigationUp(true);
//        _binding.setGroupsViewModel(_groupsViewModel);
        _binding.setLifecycleOwner(this);

        return _binding.getRoot();
    }
}
