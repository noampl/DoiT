package com.example.doit.Model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.google.android.gms.tasks.Task;

import java.util.List;

@Dao
public interface TaskDao {

    /*@Query("select * from Task")
    List<Task> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Task...Tasks);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);*/

}
