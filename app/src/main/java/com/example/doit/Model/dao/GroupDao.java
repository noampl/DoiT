package com.example.doit.Model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import com.example.doit.Model.Group;

@Dao
public interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Group...Groups);

    @Update
    void update(Group group);

    @Delete
    void delete(Group group);
}
