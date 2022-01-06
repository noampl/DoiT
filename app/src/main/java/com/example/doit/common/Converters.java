package com.example.doit.common;

import androidx.room.TypeConverter;

import com.example.doit.Model.Group;
import com.example.doit.Model.User;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Converters {

        @TypeConverter
        public static String userToString(User user) {
            Gson gson = new GsonBuilder().create();
            return gson.toJson(user);
        }

        @TypeConverter
        public static String userListToString(List<User> users) {
            Gson gson = new GsonBuilder().create();
            return gson.toJson(users);
        }

        @TypeConverter
        public static User StringToUser(String val) {
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(val,User.class);
        }

        @TypeConverter
        public static List<User> StringToUserList(String val) {
            List<User> users = new ArrayList<>();
            Gson gson = new GsonBuilder().create();
            users.add(gson.fromJson(val,User.class));
            return users;
        }

        @TypeConverter
        public static String taskToString(Task task) {
            Gson gson = new GsonBuilder().create();
            return gson.toJson(task);
        }

        @TypeConverter
        public static String taskListToString(List<Task> tasks) {
            Gson gson = new GsonBuilder().create();
            return gson.toJson(tasks);
        }

        @TypeConverter
        public static Task StringToTask(String val) {
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(val,Task.class);
        }

        @TypeConverter
        public static List<Task> StringToTaskList(String val) {
            List<Task> tasks = new ArrayList<>();
            Gson gson = new GsonBuilder().create();
            tasks.add(gson.fromJson(val,Task.class));
            return tasks;
        }

        @TypeConverter
        public static String groupListToString(List<Group> groups) {
            Gson gson = new GsonBuilder().create();
            return gson.toJson(groups);
        }

        @TypeConverter
        public static List<Group> StringToGroupList(String val) {
            List<Group> groups = new ArrayList<>();
            Gson gson = new GsonBuilder().create();
            groups.add(gson.fromJson(val,Group.class));
            return groups;
        }


        @TypeConverter
        public static String groupToString(Group group) {
            Gson gson = new GsonBuilder().create();
            return gson.toJson(group);
        }

        @TypeConverter
        public static Group StringToGroup(String val) {
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(val,Group.class);
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

