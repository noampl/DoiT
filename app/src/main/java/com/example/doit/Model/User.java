package com.example.doit.Model;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


import com.example.doit.common.Roles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Entity
public class User {

    // region Members

    private final String TAG = "User Model";
    private final String NO_IMAGE = "no_image";
    private final String ISRAEL_COUNRTY_CODE = "+972";
    @PrimaryKey
    @NonNull
    private String _id;
    private String _email;
    private String _lastName;
    private String _firstName;
    private String _password;
    private String _image;
    private String _phone;
    private String _countryPhoneCode;
    private Roles _role;
    private List<Group> _groups;

    // endregion

    // region C'tor

    public User() {
    }

    public User(String id,String _email, String firstName, String _lastName, String _password, String _image, String _phone,
                String _countryPhoneCode, Roles _role, List<Group> groups) {
        this._id = id;
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

    public String get_id() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
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

    public void setPhoneCountryCode(String _countryPhoneCode) {
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
        user.put("id",get_id());
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

}
