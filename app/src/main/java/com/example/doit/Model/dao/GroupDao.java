package com.example.doit.Model.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverter;
import androidx.room.Update;

import com.example.doit.Model.Group;
import com.example.doit.Model.User;
import com.example.doit.common.Converters;

import java.util.List;

@Dao
public interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Group...Groups);

    @Update
    void update(Group group);

    @Delete
    void delete(Group group);
}
