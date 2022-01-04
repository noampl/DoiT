package com.example.doit.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.doit.common.Converters;

import java.util.List;

@Entity
public class Group {

    // region Members
    @PrimaryKey
    @NonNull
    private String _id; // the id of the firebase
    private String _name;
    private String _description;
    @TypeConverters(Converters.class)
    private List<User> _users;
    @TypeConverters(Converters.class)
    private List<User> admins;
    private String _image;
    @TypeConverters(Converters.class)
    private List<Task> tasks;

    // endregion

    // reigon C'tor

    public Group(){}

    public Group(String id, String _name, String _description, List<User> _users, List<User> _admins, String _image, List<Task> _tasks) {
        this._id = id;
        this._name = _name;
        this._description = _description;
        this._users = _users;
        this.admins = _admins;
        this._image = _image;
        this.tasks = _tasks;
    }

    // endregion

    // region Properties

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public List<User> get_users() {
        return _users;
    }

    public void set_users(List<User> _users) {
        this._users = _users;
    }

    public List<User> getAdmins() {
        return admins;
    }

    public void setAdmins(List<User> admins) {
        this.admins = admins;
    }

    public String get_image() {
        return _image;
    }

    public void set_image(String _image) {
        this._image = _image;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    // endregion
}
