package com.example.doit.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
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
    private List<String> membersId;
    private List<String> _adminsId;
    private List<String> _tasksId;
    private String _image;

    // endregion

    // region C'tor

    public Group(){}

    public Group(String id, String _name, String _description, List<String> _membersID, List<String> _admins, String _image, List<String> _tasks) {
        this._groupId = id;
        this._name = _name;
        this._description = _description;
        this.membersId = _membersID;
        this._adminsId = _admins; //TODO: Noam -> delete?
        this._image = _image;
        this._tasksId = _tasks;
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

    public List<String> getMembersId() {
        return membersId;
    }

    public void setMembersId(List<String> membersId) {
        this.membersId = membersId;
    }

    public List<String> get_adminsId() {
        return _adminsId;
    }

    public void set_adminsId(List<String> _adminsId) {
        this._adminsId = _adminsId;
    }

    public String get_image() {
        return _image;
    }

    public void set_image(String _image) {
        this._image = _image;
    }

    public List<String> get_tasksId() {
        return _tasksId;
    }

    public void set_tasksId(List<String> _tasksId) {
        this._tasksId = _tasksId;
    }

    // endregion

    //region Public Methods

    public HashMap<String, Object> create(){
        HashMap<String, Object> groupMap = new HashMap<>();
        groupMap.put("id",_groupId);
        groupMap.put("name",_name);
        groupMap.put("description",_description);
        groupMap.put("image",_image);
        groupMap.put("membersId",membersId);
        groupMap.put("adminsId",_adminsId);
        groupMap.put("taskId",_tasksId);

        return groupMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return _groupId.equals(group._groupId) && _name.equals(group._name) && _description.equals(group._description) && Objects.equals(membersId, group.membersId) && Objects.equals(_adminsId, group._adminsId) && Objects.equals(_image, group._image) && Objects.equals(_tasksId, group._tasksId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_groupId, _name, _description, membersId, _adminsId, _image, _tasksId);
    }

    // endregion


}
