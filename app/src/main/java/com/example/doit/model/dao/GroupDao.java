package com.example.doit.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.doit.model.entities.Group;
import com.example.doit.model.entities.User;

import java.util.List;

@Dao
public interface GroupDao {

    @Query("SELECT * FROM `group`")
    List<Group> getAll();

    @Query("DELETE FROM `group`")
    void deleteAll();

    @Query("SELECT * FROM `group` WHERE :userId in (membersId)")
    LiveData<List<Group>> getAllUserGroups(String userId);

    @Query("SELECT * FROM `group` WHERE _groupId = :groupId")
    Group getGroup(String groupId);

    @Query("SELECT * FROM `group` JOIN user WHERE _groupId = :groupId AND _userId ")
    List<User> getGroupMembers(String groupId);

    @Query("SELECT membersId FROM `group` WHERE :groupId = _groupId")
    List<String> getMembersId(String groupId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Group...Groups);

    @Query("DELETE FROM `group` WHERE :userId NOT IN (membersId) ")
    void deleteWhereNotExist(String userId);

    @Update
    void update(Group group);

    @Delete
    void delete(Group group);

    @Query("DELETE FROM `group` WHERE :groupId = _groupId")
    void delete(String groupId);

    @Query("SELECT SUM(_value) FROM `task` WHERE _taskId IN (:membersId) AND _finishDate > 0")
    Integer getSumTasksUsersValuesFromGroup(List<String> membersId);
}
