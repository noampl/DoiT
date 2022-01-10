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

    @Query("DELETE FROM Task WHERE _taskId = :taskId")
    void deleteTaskById(String taskId);

    @Query("select * from Task WHERE _assigneeId =:assigneeId")
    List<Task> getTasksByAssignee(String assigneeId);

    @Query("select * from Task WHERE _groupId =:groupId")
    List<Task> getTasksByGroup(String groupId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Task...Tasks);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

}
