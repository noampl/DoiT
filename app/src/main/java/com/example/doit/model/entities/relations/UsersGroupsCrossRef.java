package com.example.doit.model.entities.relations;

import androidx.annotation.NonNull;
import androidx.room.Entity;

/**
 * Helper for many to many relation
 */
@Entity(primaryKeys = {"_userId","_groupId"})
public class UsersGroupsCrossRef {

    @NonNull
    private String _userId;
    @NonNull
    private String _groupId;

    public String get_userId() {
        return _userId;
    }

    public void set_userId(String _userId) {
        this._userId = _userId;
    }

    public String get_groupId() {
        return _groupId;
    }

    public void set_groupId(String _groupId) {
        this._groupId = _groupId;
    }
}
