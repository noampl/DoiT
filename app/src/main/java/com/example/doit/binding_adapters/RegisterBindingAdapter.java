package com.example.doit.binding_adapters;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;


public class RegisterBindingAdapter {
    @BindingAdapter("visibility")
    public static void setVisibility(View v, Boolean visibility) {
        if (visibility) { v.setVisibility(View.VISIBLE); }
        else { v.setVisibility(View.GONE); }
    }
}
