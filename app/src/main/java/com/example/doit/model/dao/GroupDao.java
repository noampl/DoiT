package com.example.doit.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.doit.model.entities.Group;
import com.example.doit.model.entities.relations.GroupWithTasks;
import com.example.doit.model.entities.relations.UserWithGroups;

import java.util.List;

@Dao
public interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Group...Groups);

    @Update
    void update(Group group);

    @Delete
    void delete(Group group);

    @Transaction
    @Query("SELECT * FROM `group` WHERE _groupId = :groupId")
    List<GroupWithTasks> getGroupWithTasks(String groupId);

    @Transaction
    @Query("SELECT * FROM `user` WHERE _userId = :userId")
    List<UserWithGroups> getUserWithGroups(String userId);
}
