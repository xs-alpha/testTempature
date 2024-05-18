package com.xiaosheng.testtempature.dao;


import com.xiaosheng.testtempature.dao.mapper.TempatureHistoryMapper;
import com.xiaosheng.testtempature.dao.mapper.UserMapper;

public class MyApplication {
    public static UserDataBase userDataBase;
    public static TempatureDataBase tempatureDataBase;

    public static UserMapper userMapper;

    public static TempatureHistoryMapper tempatureHistoryMapper;

    public static UserDataBase getDb(){
        return userDataBase;
    }

    public static TempatureDataBase getLogDb(){
        return tempatureDataBase;
    }
}
