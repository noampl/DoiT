package com.example.doit.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.doit.common.Converters;

import java.util.Date;

@Entity
public class Task {

    // region Members

    @PrimaryKey
    @NonNull
    private String _taskId;
    private String _groupId;
    private String _name;
    private String _description;
    @TypeConverters(Converters.class)
    private Date _createdDate;
    @TypeConverters(Converters.class)
    private Date _targetDate;
    @TypeConverters(Converters.class)
    private Date _finishDate;
    private String _createdById;
    private String _assigneeId;
    private int _value;
    private String _image;

    // endregion

    // region C'tor

    public Task(){
    }

    @Ignore
    public Task(String taskId, String _groupID, String _name, String _description, Date _createdDate, Date _targetDate, String _createdBy, String _assignee, int _value, String _image) {
        this._taskId = taskId;
        this._groupId = _groupID;
        this._name = _name;
        this._description = _description;
        this._createdDate = _createdDate;
        this._targetDate = _targetDate;
        this._createdById = _createdBy;
        this._assigneeId = _assignee;
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

    public String get_createdById() {
        return _createdById;
    }

    public void set_createdById(String _createdById) {
        this._createdById = _createdById;
    }

    public String get_assigneeId() {
        return _assigneeId;
    }

    public void set_assigneeId(String _assigneeId) {
        this._assigneeId = _assigneeId;
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
