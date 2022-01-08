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
import androidx.lifecycle.ViewModelProvider;

import com.example.doit.R;
import com.example.doit.databinding.AdditionDialogFragmentBinding;
import com.example.doit.viewmodel.GroupsViewModel;

public class AdditionDialog extends DialogFragment {

    // region Members

    private AdditionDialogFragmentBinding _binding;
    private AlertDialog _dialog;

    // endregion

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
        _dialog.setView(_binding.getRoot());
        _dialog.setCancelable(false);

        return _binding.getRoot();
    }
}
