package com.example.doit.Model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.doit.Model.dao.GroupDao;
import com.example.doit.Model.dao.TaskDao;
import com.example.doit.Model.dao.UserDao;
import com.example.doit.view.MainActivity;

@Database(entities = {Group.class, User.class, Task.class}, version = 1)
abstract class DoitLocalDB extends RoomDatabase {
    public abstract GroupDao groupDao();
    public abstract TaskDao taskDao();
    public abstract UserDao userDao();

}

class LocalDB {
    static public DoitLocalDB db =
            Room.databaseBuilder(MainActivity.getContext(),
                    DoitLocalDB.class,
                    "DoitLocalDB.db")
                    .fallbackToDestructiveMigration()
                    .build();
}