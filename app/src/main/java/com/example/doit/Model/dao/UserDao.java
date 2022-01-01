package com.example.doit.Model.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.doit.Model.User;

import java.util.List;

public interface UserDao {

    @Query("select * from user")
    MutableLiveData<List<User>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User...Users);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("select * from user where _id = :id")
    MutableLiveData<User> loadUserById(String id);
}
