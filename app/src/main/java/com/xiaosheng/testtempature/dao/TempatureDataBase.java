package com.xiaosheng.testtempature.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.xiaosheng.testtempature.dao.mapper.TempatureHistoryMapper;
import com.xiaosheng.testtempature.dao.mapper.UserMapper;
import com.xiaosheng.testtempature.entity.Tempature;
import com.xiaosheng.testtempature.entity.TempatureHistory;

@Database(entities = { TempatureHistory.class},version = 2,exportSchema = false)
public abstract class TempatureDataBase extends RoomDatabase {
    // 获取数据库中某张表的持久化对象
    public abstract TempatureHistoryMapper tempatureHistoryMapper();
}
