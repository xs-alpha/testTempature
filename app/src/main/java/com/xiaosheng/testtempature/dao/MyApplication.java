package com.xiaosheng.testtempature.dao;


import com.xiaosheng.testtempature.dao.mapper.UserMapper;

public class MyApplication {
    public static UserDataBase userDataBase;

    public static UserMapper userMapper;

    public static UserDataBase getDb(){
        return userDataBase;
    }

}
