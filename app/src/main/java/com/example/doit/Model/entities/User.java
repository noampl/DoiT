package com.example.doit.Model.entities;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;


import com.example.doit.common.Converters;
import com.example.doit.common.Roles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Entity
public class User {

    // region Members
    @Ignore
    private static final String TAG = "User Model";
    @Ignore
    private static final String NO_IMAGE = "no_image";
    @Ignore
    private static final String ISRAEL_COUNRTY_CODE = "+972";
    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String _userId;
    private String _email;
    private String _lastName;
    private String _firstName;
    private String _password;
    private String _image;
    private String _phone;
    private String _countryPhoneCode;
    @TypeConverters(Converters.class)
    private Roles _role;
    @TypeConverters(Converters.class)
    private List<Group> _groups;

    // endregion

    // region C'tor

    public User() {
    }

    @Ignore
    public User(@NonNull String id, String _email, String firstName, String _lastName, String _password, String _image, String _phone,
                String _countryPhoneCode, Roles _role, List<Group> groups) {
        this._userId = id;
        this._email = _email;
        this._lastName = _lastName;
        this._password = _password;
        this._image = _image;
        this._phone = _phone;
        this._countryPhoneCode = _countryPhoneCode;
        this._role = _role;
        this._firstName = firstName;
        this._groups = groups;
    }

    // endregion

    // endregion Properties

    public String get_userId() {
        return _userId;
    }

    public String get_email() {
        return _email;
    }

    public void setEmail(String _email) {
        this._email = _email;

    }

    public String get_lastName() {
        return _lastName;
    }

    public void setLastName(String _lastName) {
        this._lastName = _lastName;
    }

    public String get_firstName() {
        return _firstName;
    }

    public void setFirstName(String _firstName) {
        this._firstName = _firstName;
    }

    public String get_password() {
        return _password;
    }

    public void setImgae(String image_path){
        _image = image_path;
    }

    public void set_userId(@NonNull String _userId) {
        this._userId = _userId;
    }

    public void setPhoneCountryCode(String phoneCountryCode) {
        _countryPhoneCode = phoneCountryCode;
    }

    public void setPassword(String _password) {
        this._password = _password;
    }

    public String get_image() {
        return _image;
    }

    public void setImage(String _image) {
        this._image = _image;
    }

    public String get_phone() {
        return _phone;
    }

    public void setPhone(String _phone) {
        this._phone = _phone;
    }

    public String get_countryPhoneCode() {
        return _countryPhoneCode;
    }

    public void setCountryPhoneCode(String _countryPhoneCode) {
        this._countryPhoneCode = _countryPhoneCode;
    }

    public Roles get_role() {
        return _role;
    }

    public void setRole(Roles _role) {
        this._role = _role;
    }

    public List<Group> get_groups() {
        return _groups;
    }

    public void set_groups(List<Group> _groups) {
        this._groups = _groups;
    }

    // endregion



    //todo: add setImage

    public Map<String, Object> create(){
        Map<String, Object> user = new HashMap<String, Object>();
        user.put("id", get_userId());
        user.put("firstName",get_firstName());
        user.put("lastName",get_lastName());
        user.put("email",get_email());
        user.put("password",get_password());
        user.put("role",get_role());
        user.put("phone",get_phone());
        user.put("countryCode",get_countryPhoneCode());
        user.put("image",get_image());
        user.put("groups",get_groups());


        return user;
    }

    @Override
    public String toString() {
        return "User{" +
                ", _id='" + _userId + '\'' +
                ", _email='" + _email + '\'' +
                ", _lastName='" + _lastName + '\'' +
                ", _firstName='" + _firstName + '\'' +
                ", _password='" + _password + '\'' +
                ", _image='" + _image + '\'' +
                ", _phone='" + _phone + '\'' +
                ", _countryPhoneCode='" + _countryPhoneCode + '\'' +
                ", _role=" + _role +
                ", _groups=" + _groups +
                '}';
    }
}
