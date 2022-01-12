package com.example.doit.binding_adapters;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import androidx.databinding.BindingAdapter;
import com.example.doit.viewmodel.UsersViewModel;


public class AdditionBindingAdapters {


    @BindingAdapter({"onChecked", "position"})
    public static void onChecked(CheckBox checkBox, UsersViewModel viewModel, int position) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    viewModel.addUser(position);
                }
                else{
                    viewModel.removeUser(position);
                }
            }
        });

    }
}
