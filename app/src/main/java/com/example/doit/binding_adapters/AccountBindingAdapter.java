package com.example.doit.binding_adapters;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.example.doit.R;
import com.squareup.picasso.Picasso;

public class AccountBindingAdapter {
        @BindingAdapter("image")
        public static void setImage(ImageButton v, String image) {
            if (image != null) {
                Picasso.with(v.getContext()).load(image).fit().into(v);
            }
        }
    }
