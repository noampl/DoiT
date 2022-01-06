package com.example.doit.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.MyApplication;
import com.example.doit.model.dao.GroupDao;
import com.example.doit.model.dao.TaskDao;
import com.example.doit.model.dao.UserDao;
import com.example.doit.model.entities.Group;
import com.example.doit.model.entities.Task;
import com.example.doit.model.entities.User;
import com.example.doit.model.entities.relations.UsersGroupsCrossRef;

@Database(entities = {Group.class, User.class, Task.class, UsersGroupsCrossRef.class}, version = 1, exportSchema = false)
abstract class DoitLocalDB extends RoomDatabase {
    public abstract GroupDao groupDao();
    public abstract TaskDao taskDao();
    public abstract UserDao userDao();

}

class LocalDB {
    static public DoitLocalDB db =
            Room.databaseBuilder(MyApplication.getAppContext(),
                    DoitLocalDB.class,
                    "DoitLocalDB.db")
                    .fallbackToDestructiveMigration()
                    .build();
}