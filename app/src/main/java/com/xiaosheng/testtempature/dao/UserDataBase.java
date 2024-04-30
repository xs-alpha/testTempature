package com.xiaosheng.testtempature.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.xiaosheng.testtempature.dao.mapper.UserMapper;
import com.xiaosheng.testtempature.entity.Tempature;

@Database(entities = {Tempature.class},version = 6,exportSchema = false)
public abstract class UserDataBase extends RoomDatabase {
    // 获取数据库中某张表的持久化对象
    public abstract UserMapper userMapper();
}
