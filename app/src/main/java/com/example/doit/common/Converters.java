package com.example.doit.common;

import androidx.room.TypeConverter;

import com.example.doit.Model.User;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Converters {
        @TypeConverter
        public static List<User> adminToList(User user) {
            List<User> users = new ArrayList<>();
            users.add(user);
            return users;
        }

        @TypeConverter
        public static User listToAdmin(List<User> users) {
            return users.get(0);
        }
}

