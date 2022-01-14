package com.example.doit.binding_adapters;

import android.annotation.SuppressLint;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TaskBindingAdapter {

    @BindingAdapter("date")
    public static void setDate(TextView v, long date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            v.setText((CharSequence) sdf.parse(new Date(date).toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @BindingAdapter("setImage")
    public static void setImage(ImageView imageView, String url){
        if (url != null && url.length() > 5) {
            Picasso.with(imageView.getContext()).load(url).fit().into(imageView);
        }
    }



}
