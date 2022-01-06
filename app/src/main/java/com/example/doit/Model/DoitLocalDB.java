package com.example.doit.Model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.MyApplication;
import com.example.doit.Model.dao.GroupDao;
import com.example.doit.Model.dao.TaskDao;
import com.example.doit.Model.dao.UserDao;
import com.example.doit.Model.entities.Group;
import com.example.doit.Model.entities.Task;
import com.example.doit.Model.entities.User;
import com.example.doit.Model.entities.relations.UsersGroupsCrossRef;

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