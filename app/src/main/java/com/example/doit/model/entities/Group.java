package com.example.doit.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.doit.common.Converters;

import java.util.List;
import java.util.Objects;

@Entity
public class Group {

    // region Members
    @PrimaryKey
    @NonNull
    private String _groupId; // the id of the firebase
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
        this._groupId = id;
        this._name = _name;
        this._description = _description;
        this._users = _users;
        this.admins = _admins;
        this._image = _image;
        this.tasks = _tasks;
    }

    // endregion

    // region Properties

    public String get_groupId() {
        return _groupId;
    }

    public void set_groupId(String _groupId) {
        this._groupId = _groupId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return _groupId.equals(group._groupId) && _name.equals(group._name) && _description.equals(group._description) && Objects.equals(_users, group._users) && Objects.equals(admins, group.admins) && Objects.equals(_image, group._image) && Objects.equals(tasks, group.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_groupId, _name, _description, _users, admins, _image, tasks);
    }

    // endregion
}