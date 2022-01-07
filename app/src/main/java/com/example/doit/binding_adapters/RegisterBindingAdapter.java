package com.example.doit.binding_adapters;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;

import java.util.Objects;
import java.util.regex.Pattern;


public class RegisterBindingAdapter {

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
}
