package com.example.doit.model.entities;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;


import com.example.doit.common.Converters;
import com.example.doit.common.Roles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
public class User {

    // region Members
    @Ignore
    private static final String TAG = "User Model";
    @Ignore
    private static final String NO_IMAGE = "no_image";

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String _userId;
    private String _email;
    private String _lastName;
    private String _firstName;
    private String _password;
    private String _image;
    @TypeConverters(Converters.class)
    private Roles _role;
    private List<String> _groupsId;
    // endregion

    // region C'tor

    public User() {
        _role = Roles.CLIENT;
    }

    @Ignore
    public User(@NonNull String id, String _email, String firstName, String _lastName, String _password, String _image, Roles _role, List<String> groups) {
        this._userId = id;
        this._email = _email;
        this._lastName = _lastName;
        this._password = _password;
        this._image = _image;
        this._role = _role;
        this._firstName = firstName;
        this._groupsId = groups;
    }

    // endregion

    // region Properties

    public String get_userId() {
        if (_userId == null)
            _userId = "";
        return _userId;
    }

    public String get_email() {
        if (_email == null)
            _email = "";
        return _email;
    }

    public void setEmail(String _email) {
        this._email = _email;

    }
    public String get_fullName(){
        return _firstName + " " + _lastName;
    }

    public String get_lastName() {
        if (_lastName == null)
            _lastName = "";
        return _lastName;
    }

    public void setLastName(String _lastName) {
        this._lastName = _lastName;
    }

    public String get_firstName() {
        if (_firstName == null)
            _firstName = "";
        return _firstName;
    }

    public void setFirstName(String _firstName) {
        this._firstName = _firstName;
    }

    public String get_password() {
        if (_password == null)
            _password = "";
        return _password;
    }

    public void set_userId(@NonNull String _userId) {
        this._userId = _userId;
    }

    public void addGroupOrUpdate(Group group){
        for(String groupId : get_groupsId()){
            if(Objects.equals(groupId, group.get_groupId())){
                return;
            }
        }
        get_groupsId().add(group.get_groupId());
    }

    public void setPassword(String _password) {
        this._password = _password;
    }

    public String get_image() {
        if (_image == null)
            _image = "";
        return _image;
    }

    public void set_image(String _image) {
        this._image = _image;
    }

    public Roles get_role() {
        return _role;
    }

    public void setRole(Roles _role) {
        this._role = _role;
    }

    public List<String> get_groupsId() {
        if(_groupsId == null) { _groupsId = new ArrayList<>(); }
        return _groupsId;
    }

    public void set_groupsId(List<String> _groupsId) {
        this._groupsId = _groupsId;
    }

    // endregion

    // region Public Methods

    public Map<String, Object> create(){
        Map<String, Object> user = new HashMap<String, Object>();
        user.put("id", get_userId());
        user.put("firstName",get_firstName());
        user.put("lastName",get_lastName());
        user.put("role",get_role());
        user.put("image",get_image());
        user.put("groups", get_groupsId());


        return user;
    }

    // endregion

    // region Object

    @Override
    public String toString() {
        return "User{" +
                ", _id='" + _userId + '\'' +
                ", _email='" + _email + '\'' +
                ", _lastName='" + _lastName + '\'' +
                ", _firstName='" + _firstName + '\'' +
                ", _password='" + _password + '\'' +
                ", _image='" + _image + '\'' +
                ", _role=" + _role +
                ", _groups=" + _groupsId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return _userId.equals(user.get_userId()) && _email.equals(user.get_email()) && Objects.equals(_lastName, user.get_lastName()) &&
                _firstName.equals(user.get_firstName()) && Objects.equals(_password, user.get_password()) &&
                Objects.equals(_image, user.get_image()) && Objects.equals(_groupsId, user.get_groupsId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(_userId, _email, _lastName, _firstName, _password, _image, _role, _groupsId);
    }

    // endregion
}
