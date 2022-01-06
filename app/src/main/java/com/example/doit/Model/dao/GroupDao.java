package com.example.doit.Model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.doit.Model.entities.Group;
import com.example.doit.Model.entities.relations.GroupWithTasks;
import com.example.doit.Model.entities.relations.UserWithGroups;
import com.example.doit.Model.entities.relations.UsersGroupsCrossRef;

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
