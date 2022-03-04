package com.example.doit.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.doit.model.entities.User;
import java.util.List;

@Dao
public interface UserDao {

    @Query("select * from user")
    List<User> getAll();

    @Query("DELETE FROM user")
    void deleteAll();

    @Query("SELECT * FROM user WHERE :groupId IN (_groupsId)") //TODO fix that
    List<User> getUsersByGroup(String groupId);

    @Query("select * from user where _userId = :userId")
    User getUserById(String userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User...Users);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

}
