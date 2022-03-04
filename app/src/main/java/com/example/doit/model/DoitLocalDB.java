package com.example.doit.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.MyApplication;
import com.example.doit.common.Converters;
import com.example.doit.model.dao.GroupDao;
import com.example.doit.model.dao.TaskDao;
import com.example.doit.model.dao.UserDao;
import com.example.doit.model.entities.Group;
import com.example.doit.model.entities.Task;
import com.example.doit.model.entities.User;


@Database(entities = {Group.class, User.class, Task.class }, version = 8, exportSchema = false)
@TypeConverters({Converters.class})
abstract class DoitLocalDB extends RoomDatabase {
    public abstract GroupDao groupDao();
    public abstract TaskDao taskDao();
    public abstract UserDao userDao();

}

class LocalDB {
    static volatile public DoitLocalDB db =
            Room.databaseBuilder(MyApplication.getAppContext(),
                    DoitLocalDB.class,
                    "DoitLocalDB.db")
                    .fallbackToDestructiveMigration().allowMainThreadQueries()
                    .build();
}