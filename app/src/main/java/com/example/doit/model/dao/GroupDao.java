package com.example.doit.model.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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

    @Query("SELECT * FROM `group`")
    List<Group> getAll();

    @Query("SELECT * FROM `group` WHERE _groupId = :groupId")
    Group getGroup(String groupId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Group...Groups);

    @Query("DELETE FROM `group` WHERE :userId NOT IN (membersId) ")
    void deleteWhereNotExist(String userId);

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

    @Query("SELECT SUM(_value) FROM `task` WHERE _taskId IN (:membersId) AND _finishDate > 0")
    Integer getSumTasksUsersValuesFromGroup(List<String> membersId);
}
