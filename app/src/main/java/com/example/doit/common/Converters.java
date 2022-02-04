package com.example.doit.common;

import androidx.room.TypeConverter;

import com.example.doit.model.entities.Group;
import com.example.doit.model.entities.Task;
import com.example.doit.model.entities.User;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Converters {

        @TypeConverter
        public static List<String> fromString(String value) {
            Type listType = new TypeToken<List<String>>() {}.getType();
            return new ArrayList<>(new Gson().fromJson(value, listType));
        }

        @TypeConverter
        public static String fromList(List<String> list) {
            Gson gson = new Gson();
            String json = gson.toJson(list);
            return json;
        }

        @TypeConverter
        public static int RoleToInt(Roles role){
            return role.ordinal();
        }

        @TypeConverter
        public static Roles intToRole(int val){
            return Roles.values()[val];
        }

        @TypeConverter
        public static long dateToLong(Date date){
            return date == null ? null : date.getTime();
        }

        @TypeConverter
        public static Date longToDate(long val){
            return val == 0 ? null : new Date(val);
        }


}

