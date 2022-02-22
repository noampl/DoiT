package com.example.doit.view.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.doit.R;
import com.example.doit.databinding.AdditionDialogFragmentBinding;
import com.example.doit.model.entities.User;
import com.example.doit.view.adapters.AdditionAdapter;
import com.example.doit.viewmodel.UsersViewModel;

import java.util.ArrayList;
import java.util.List;

public class AdditionDialog extends DialogFragment implements SearchView.OnQueryTextListener {

    // region Members

    private AdditionDialogFragmentBinding _binding;
    private AlertDialog _dialog;
    private UsersViewModel usersViewModel;
    private boolean _isRemoteSearcher;

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
        String groupId = AdditionDialogArgs.fromBundle(getArguments()).getGroupId();
        _isRemoteSearcher = AdditionDialogArgs.fromBundle(getArguments()).getIsRemoteSearcher();
        _binding = DataBindingUtil.inflate(inflater, R.layout.addition_dialog_fragment, container, false);
        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        usersViewModel.set_selectedUser(new ArrayList<>());

        if(groupId != null && !groupId.equals("") && groupId.length() > 5){
            usersViewModel.setUsersById(groupId);
        }

        _dialog.setView(_binding.getRoot());
        _dialog.setCancelable(true);
        _dialog.setTitle("Select User");
        _binding.searchBox.setOnQueryTextListener(this);
        initListeners();
        _binding.setLifecycleOwner(this);
        return _binding.getRoot();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query != null && _isRemoteSearcher) {
            usersViewModel.searchUsers(query);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText != null && _isRemoteSearcher) {
            usersViewModel.searchUsers(newText);
        }
        return true;
    }

    private void initListeners() {
        AdditionAdapter adapter = new AdditionAdapter(AdditionDialogArgs.fromBundle(getArguments()).getIsChecked());
        adapter.set_usersViewModel(usersViewModel);
        usersViewModel.get_users().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<User> users) {
                adapter.submitList(users);
                adapter.notifyDataSetChanged();
            }
        });
        _binding.listItem.setAdapter(adapter);

        _binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usersViewModel.set_users(new ArrayList<User>());
                Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigateUp();
            }
        });

        _binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usersViewModel.submitUsers();
                Navigation.findNavController(requireActivity(), R.id.fragmentContainerView).navigateUp();
            }
        });
    }

}
