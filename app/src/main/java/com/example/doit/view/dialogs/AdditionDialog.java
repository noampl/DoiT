package com.example.doit.view.dialogs;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.doit.R;
import com.example.doit.databinding.AdditionDialogFragmentBinding;
import com.example.doit.model.entities.User;
import com.example.doit.view.adapters.AdditionAdapter;
import com.example.doit.viewmodel.UsersViewModel;

import java.util.List;

public class AdditionDialog extends DialogFragment implements SearchView.OnQueryTextListener {

    // region Members

    private AdditionDialogFragmentBinding _binding;
    private AlertDialog _dialog;
    private UsersViewModel usersViewModel;

    // endregion

    @RequiresApi(api = Build.VERSION_CODES.Q)
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
        _binding = DataBindingUtil.inflate(inflater, R.layout.addition_dialog_fragment, container, false);
        _binding.setLifecycleOwner(this);
        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        _dialog.setView(_binding.getRoot());
        _dialog.setCancelable(true);
        _binding.searchBox.setOnQueryTextListener(this);
        initListeners();
        return _binding.getRoot();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query != null) {
            usersViewModel.searchUsers(query);
        }

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText != null){
            usersViewModel.searchUsers(newText);
        }
        return false;
    }

    private void initListeners() {
        AdditionAdapter adapter = new AdditionAdapter();
        _binding.listItem.setAdapter(adapter);
        usersViewModel.get_users().observe(requireActivity(), new Observer<List<User>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<User> users) {
                adapter.submitList(users);
                if (users.size() > 3) {
//                    _binding.listItem.setH
                }
                adapter.notifyDataSetChanged();
                Log.d("PELEG", "submit users to addotion dialog");
            }
        });

        _binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usersViewModel.get_users().getValue().clear();
                dismiss();
            }
        });
    }
}
