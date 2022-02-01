package com.example.doit.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.example.doit.model.entities.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("select * from Task")
    List<Task> getAll();

    @Query("DELETE FROM Task")
    void deleteAll();

    @Query("select * from Task WHERE _taskId = :taskId")
    Task getTaskById(String taskId);

    @Query("DELETE FROM Task WHERE _taskId = :taskId")
    void deleteTaskById(String taskId);

    @Query("select * from Task WHERE _assigneeId =:assigneeId")
    List<Task> getTasksByAssignee(String assigneeId);

    @Query("select * from Task WHERE _groupId =:groupId")
    List<Task> getTasksByGroup(String groupId);

    @Query("SELECT SUM(_value) FROM `Task` WHERE _groupId = :groupId AND _assigneeId = :userId AND _finishDate != 0")
    int getUserScoreByGroup(String userId, String groupId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Task...Tasks);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM Task where _groupId NOT IN (:groups)")
    void deleteTaskWhichItsGroupNotExist(List<String> groups);

}
