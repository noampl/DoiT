package com.example.doit.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.doit.common.Converters;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

@Entity
public class Task {

    // region Members

    @PrimaryKey
    @NonNull
    private String _taskId;
    private String _groupId;
    private String _name;
    private String _description;
    private long _createdDate;
    private long _targetDate;
    private long _finishDate;
    private String _createdById;
    private String _assigneeId;
    private int _value;
    private String _image;

    // endregion

    // region C'tor

    public Task(){
    }

    @Ignore
    public Task(String taskId, String _groupID, String _name, String _description, long _createdDate, long _targetDate, String _createdBy, String _assignee, int _value, String _image) {
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
        if (_taskId == null){_taskId="";}
        return _taskId;
    }

    public void set_taskId(String _taskId) {
        this._taskId = _taskId;
    }

    public String get_groupId() {
        if (_groupId ==null){_groupId= "";}
        return _groupId;
    }

    public void set_groupId(String _groupId) {
        this._groupId = _groupId;
    }

    public String get_name() {
        if (_name== null){_name= "";}
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_description() {
        if (_description == null){_description="";}
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public long get_createdDate() {
        return _createdDate;
    }

    public void set_createdDate(long _createdDate) {
        this._createdDate = _createdDate;
    }

    public long get_targetDate() {
        return _targetDate;
    }

    public void set_targetDate(long _targetDate) {
        this._targetDate = _targetDate;
    }

    public long get_finishDate() {
        return _finishDate;
    }

    public void set_finishDate(long _finishDate) {
        this._finishDate = _finishDate;
    }

    public String get_createdById() {
        if (_createdById == null){_createdById ="";}
        return _createdById;
    }

    public void set_createdById(String _createdById) {
        this._createdById = _createdById;
    }

    public String get_assigneeId() {
        if (_assigneeId == null){_assigneeId="";}
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
        if (_image == null){_image="";}
        return _image;
    }

    public void set_image(String _image) {
        this._image = _image;
    }

    // endregion

    public HashMap<String, Object> create(){
        HashMap<String, Object> taskMap = new HashMap<>();
        taskMap.put("id",_taskId);
        taskMap.put("name",_name);
        taskMap.put("description",_description);
        taskMap.put("image",_image);
        taskMap.put("createdDate",_createdDate);
        taskMap.put("targetDate",_targetDate);
        taskMap.put("groupId", _groupId);
        taskMap.put("createdByUserId",_createdById);
        taskMap.put("assigneeId",_assigneeId);
        taskMap.put("finishDate",_finishDate);
        taskMap.put("value",_value);

        return taskMap;
    }

    public void copy(Task newTask){
        set_description(newTask.get_description());
        set_name(newTask.get_name());
        set_image(newTask.get_image());
        set_targetDate(newTask.get_targetDate());
        set_finishDate(newTask.get_finishDate());
        set_value(newTask.get_value());
        set_createdDate(newTask.get_createdDate());
        set_assigneeId(newTask.get_assigneeId());
        set_createdById(newTask.get_createdById());
        set_groupId(newTask.get_groupId());
        set_taskId(newTask.get_taskId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return _createdDate == task._createdDate && _targetDate == task._targetDate && _finishDate == task._finishDate &&
                _value == task._value && get_taskId().equals(task.get_taskId()) && get_groupId().equals(task._groupId) &&
                get_name().equals(task.get_name()) && Objects.equals(get_description(), task.get_description()) &&
                get_createdById().equals(task.get_createdById()) && Objects.equals(get_assigneeId(), task.get_assigneeId()) &&
                Objects.equals(get_image(), task.get_image());
    }

    @Override
    public int hashCode() {
        return Objects.hash(_taskId, _groupId, _name, _description, _createdDate, _targetDate, _finishDate, _createdById, _assigneeId, _value, _image);
    }

    @NonNull
    @Override
    public String toString(){
        return this.create().toString();
    }
}
