package com.example.doit.Model.entities.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.doit.Model.entities.Group;
import com.example.doit.Model.entities.User;

import java.util.List;

/**
 * Helper for one to many relation
 */
public class GroupWithUsers {
    @Embedded
    Group group;
    @Relation(
            parentColumn = "_groupId",
            entityColumn = "_userId",
            associateBy = @Junction(UsersGroupsCrossRef.class)
    )
    public List<User> users;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
