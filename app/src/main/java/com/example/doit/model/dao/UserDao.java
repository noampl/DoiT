package com.example.doit.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.doit.model.entities.User;
import com.example.doit.model.entities.relations.GroupWithUsers;
import com.example.doit.model.entities.relations.UsersGroupsCrossRef;

import java.util.List;

@Dao
public interface UserDao {

    @Query("select * from user")
    LiveData<List<User>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User...Users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void Insert(UsersGroupsCrossRef usersGroupsCrossRef);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("select * from user where _userId = :id")
    LiveData<User> loadUserById(String id);

    @Transaction
    @Query("SELECT * FROM `group` WHERE _groupId = :groupId")
    List<GroupWithUsers> getGroupsWithUsers(String groupId);
}
