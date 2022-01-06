package com.example.doit.model.entities.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.doit.model.entities.Group;
import com.example.doit.model.entities.User;

import java.util.List;

/**
 * Helper for one to many relation
 */
public class UserWithGroups {
    @Embedded
    User user;
    @Relation(
            parentColumn = "_userId",
            entityColumn = "_groupId",
            associateBy = @Junction(UsersGroupsCrossRef.class)
    )
    public List<Group> groups;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
