package com.example.doit.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Task {

    // region Members
    @PrimaryKey
    private String _taskId;
    private String _groupID;
    private String _name;
    private String _description;
    private Date _createdDate;
    private Date _targetDate;
    private Date _finishDate;
    private User _createdBy;
    private User _assignee;
    private int _value;
    private String _image;

    // endregion

    // region C'tor

    public Task(){
    }

    public Task(String taskId, String _groupID, String _name, String _description, Date _createdDate, Date _targetDate, User _createdBy, User _assignee, int _value, String _image) {
        this._taskId = taskId;
        this._groupID = _groupID;
        this._name = _name;
        this._description = _description;
        this._createdDate = _createdDate;
        this._targetDate = _targetDate;
        this._createdBy = _createdBy;
        this._assignee = _assignee;
        this._value = _value;
        this._image = _image;
    }

    // endregion

    // region Properties

    public String get_taskId() {
        return _taskId;
    }

    public void set_taskId(String _taskId) {
        this._taskId = _taskId;
    }

    public String get_groupID() {
        return _groupID;
    }

    public void set_groupID(String _groupID) {
        this._groupID = _groupID;
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

    public Date get_createdDate() {
        return _createdDate;
    }

    public void set_createdDate(Date _createdDate) {
        this._createdDate = _createdDate;
    }

    public Date get_targetDate() {
        return _targetDate;
    }

    public void set_targetDate(Date _targetDate) {
        this._targetDate = _targetDate;
    }

    public Date get_finishDate() {
        return _finishDate;
    }

    public void set_finishDate(Date _finishDate) {
        this._finishDate = _finishDate;
    }

    public User get_createdBy() {
        return _createdBy;
    }

    public void set_createdBy(User _createdBy) {
        this._createdBy = _createdBy;
    }

    public User get_assignee() {
        return _assignee;
    }

    public void set_assignee(User _assignee) {
        this._assignee = _assignee;
    }

    public int get_value() {
        return _value;
    }

    public void set_value(int _value) {
        this._value = _value;
    }

    public String get_image() {
        return _image;
    }

    public void set_image(String _image) {
        this._image = _image;
    }

    // endregion

}
