package com.example.doit.view.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.doit.R;
import com.example.doit.databinding.DialogSummaryBinding;
import com.example.doit.databinding.SummaryItemBinding;
import com.example.doit.model.entities.User;
import com.example.doit.view.adapters.SummaryAdapter;
import com.example.doit.viewmodel.UsersViewModel;

import java.util.List;

public class SummaryDialog extends DialogFragment {

    // region Members

    private UsersViewModel _usersViewModel;
    private AlertDialog _dialog;
    private DialogSummaryBinding _binding;

    // endregion

    // region Public

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        _dialog = builder.create();
        return _dialog;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String groupId = SummaryDialogArgs.fromBundle(getArguments()).getGroupId();
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_summary, container, false);
        _usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
//        _usersViewModel.fetchUsers();
        _usersViewModel.setUsersById(groupId);
        _binding.setLifecycleOwner(this);
        initlisteners(groupId);
        _dialog.setView(_binding.getRoot());
        _binding.executePendingBindings();
        return _binding.getRoot();
    }

    // endregion

    // region private Methods

    private void initlisteners(String groupId){
        SummaryAdapter adapter = new SummaryAdapter(groupId);
        adapter.set_usersViewModel(_usersViewModel);
        _usersViewModel.get_users().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                adapter.submitList(users);
            }
        });
        _binding.usersList.setAdapter(adapter);

        _binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    // endregion
}
