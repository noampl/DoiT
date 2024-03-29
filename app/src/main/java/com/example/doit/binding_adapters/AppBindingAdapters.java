package com.example.doit.binding_adapters;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;

import com.example.doit.R;
import com.example.doit.model.entities.User;
import com.example.doit.viewmodel.UsersViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppBindingAdapters {

    @BindingAdapter("image")
    public static void setImage(ImageButton v, String image) {
        if (image != null && !image.equals(Uri.EMPTY.toString())) {
            Picasso.with(v.getContext()).load(image).fit().into(v);
        }
    }

    @BindingAdapter("textUpdate")
    public static void setText(TextView textView, boolean isLast){
        if(isLast){
            textView.setText(" ");
        }
    }

    @BindingAdapter({"onChecked", "user", "position", "isMultipleChoice"})
    public static void onChecked(CheckBox checkBox, UsersViewModel viewModel, User user, int position,
                                 boolean isMultipleChoice) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                synchronized (this){
                    if (compoundButton.isChecked()) {
                        if (isMultipleChoice){
                            viewModel.addUser(user, position);
                        }
                        else{
                            viewModel.switchUser(user, position);
                        }
                    }
                    else{
                        viewModel.removeUser(user, position);
                    }
                }
            }
    });

    }

    @BindingAdapter({"emailValidation", "isValid"})
    public static void checkEmailValidation(EditText editText, MutableLiveData<String> email, MutableLiveData<Boolean> isValid){
        if(email != null ){
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getValue()).matches() && email.getValue().length() > 0){
                editText.setError("Invalid email");
                isValid.postValue(false);
            }
            isValid.postValue(true);
        }
    }

    @BindingAdapter({"regularValidation", "isClicked", "isValid"})
    public static void validateRegularValue(EditText editText, MutableLiveData<String> value,
                                            boolean isClicked, MutableLiveData<Boolean> isValid){
        if(value != null){
            if(value.getValue().length() == 0 && isClicked){
                editText.setError("required");
                isValid.postValue(false);
            }
            isValid.postValue(true);
        }
    }

    @BindingAdapter({"passwordValidation", "isValid"})
    public static void validateRegularValue(EditText editText,MutableLiveData<Boolean> isIdentical,
                                            MutableLiveData<Boolean> isValid){
        if(Boolean.FALSE.equals(isIdentical.getValue())){
            editText.setError("Passwords is not identical");
            isValid.postValue(false);
        }
        isValid.postValue(true);
    }

    @BindingAdapter("attrValid")
    public static void checkIfAttrValid(Button button, MutableLiveData<Boolean> isValid){
        if(Boolean.FALSE.equals(isValid.getValue())){
            button.setEnabled(false);
            button.setClickable(false);
            button.setError("invalid values");
        } else {
            button.setEnabled(true);
            button.setClickable(true);
        }
    }

    @BindingAdapter("date")
    public static void setDate(TextView v, long date) {
        if (date > 0){
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String text = sdf.format(new Date(date));
            v.setBackgroundResource(R.color.transparent);
            v.setText(text);
        }
        else {
            v.setBackgroundResource(R.drawable.ic_baseline_date_range_24);

        }

    }

    @BindingAdapter("setUserImage")
    public static void setUserImage(ImageView imageView, String url){
        setImage(imageView,url,R.drawable.no_profile_picture);
    }

    @BindingAdapter("setTaskImage")
    public static void setTaskImage(ImageView imageView, String url){
        setImage(imageView,url,R.drawable.to_do_list_black);
    }

    @BindingAdapter("setTaskLargeImage")
    public static void setTaskLargeImage(ImageView imageView, String url){
        setLargeImage(imageView,url,R.drawable.to_do_list_black);
    }

    @BindingAdapter("setGroupImage")
    public static void setGroupImage(ImageView imageView, String url){
        setImage(imageView,url,R.drawable.multiple_users);
    }

    @BindingAdapter("setGroupLargeImage")
    public static void setGroupLargeImage(ImageView imageView, String url){
        setLargeImage(imageView,url,R.drawable.multiple_users);
    }

    private static void setImage(ImageView imageView, String url, int Res){
        if (url != null && url.length() > 5 && !url.equals("")) {
            Picasso.with(imageView.getContext()).load(url).fit().into(imageView);
        }
        else{
            Picasso.with(imageView.getContext()).load(Res).fit().into(imageView);
        }
    }

    private static void setLargeImage(ImageView imageView, String url, int res){
        if (url != null && url.length() > 5 && !url.equals("")) {
            Picasso.with(imageView.getContext()).load(url).into(imageView);
        }
        else{
            Picasso.with(imageView.getContext()).load(res).into(imageView);
        }
    }
}