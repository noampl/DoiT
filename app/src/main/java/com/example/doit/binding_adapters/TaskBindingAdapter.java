package com.example.doit.binding_adapters;

import android.annotation.SuppressLint;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TaskBindingAdapter {

    @BindingAdapter("date")
    public static void setImage(TextView v, long date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            v.setText((CharSequence) sdf.parse(new Date(date).toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


}
