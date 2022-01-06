package com.example.doit.Model.entities.relations;


import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.doit.Model.entities.Group;
import com.example.doit.Model.entities.Task;
import com.example.doit.Model.entities.User;

import java.util.List;

/**
 * Helper for one to many relation
 */
public class UserWithTasks {
    @Embedded
    User user;
    @Relation(
        parentColumn = "_userId",
        entityColumn = "_createdById"
    )
    public List<Task> tasks;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
