package com.example.doit.Model.entities.relations;


import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.doit.Model.entities.Group;
import com.example.doit.Model.entities.Task;

import java.util.List;

/**
 * Helper for one to many relation
 */
public class GroupWithTasks {
    @Embedded
    Group group;
    @Relation(
            parentColumn = "_groupId",
            entityColumn = "_groupId"
    )
    public List<Task> tasks;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
