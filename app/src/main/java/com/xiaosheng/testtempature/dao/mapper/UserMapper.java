package com.xiaosheng.testtempature.dao.mapper;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.xiaosheng.testtempature.entity.Tempature;

import java.util.List;


@Dao
public interface UserMapper {

    @Insert
    void insert(Tempature json);

    @Query("select * from tempature")
    List<Tempature> getAll();

    @Query("select * from tempature where id = :userSaveId ")
    Tempature getById(int userSaveId);

    @Update
    void update(Tempature user);

    @Delete
    void delete(Tempature tempature);
}
