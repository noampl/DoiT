package com.example.doit.binding_adapters;

import android.annotation.SuppressLint;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;

import com.example.doit.viewmodel.UsersViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppBindingAdapters {

//
//    @BindingAdapter("menuNavigationIcon")
//    public static void setNavigationIcon(MaterialToolbar toolbar, Integer ResID){
//        if (ResID == null){
//            toolbar.setNavigationIcon(null);
//        }
//        else {
//            toolbar.setNavigationIcon(ResID);
//        }
//    }
//
//    @BindingAdapter("setMenu")
//    public static void setMenu(MaterialToolbar toolbar, int resId){
//        toolbar.invalidateMenu();
//        toolbar.inflateMenu(resId);
//    }

    @BindingAdapter("image")
    public static void setImage(ImageButton v, String image) {
        if (image != null) {
            Picasso.with(v.getContext()).load(image).fit().into(v);
        }
    }

    @BindingAdapter("textUpdate")
    public static void setText(TextView textView, boolean isLast){
        if(isLast){
            textView.setText(" ");
        }
    }

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
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String text = sdf.format(new Date(date));
        v.setText(text);

    }

    @BindingAdapter("setImage")
    public static void setImage(ImageView imageView, String url){
        if (url != null && url.length() > 5 && !url.equals("")) {
            Picasso.with(imageView.getContext()).load(url).fit().into(imageView);
        }
    }

}
