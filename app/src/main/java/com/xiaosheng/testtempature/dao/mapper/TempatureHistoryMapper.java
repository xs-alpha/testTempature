package com.xiaosheng.testtempature.dao.mapper;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.xiaosheng.testtempature.entity.Tempature;
import com.xiaosheng.testtempature.entity.TempatureHistory;

import java.util.List;


@Dao
public interface TempatureHistoryMapper {

    @Insert
    void insert(TempatureHistory json);

    @Query("select * from tempature_history ORDER BY timestamp desc limit 100 ")
    List<TempatureHistory> getAll();

    @Query("select * from tempature_history where id = :userSaveId ")
    TempatureHistory getById(int userSaveId);

    @Update
    void update(TempatureHistory user);

    @Delete
    void delete(TempatureHistory tempature);

    @Query("DELETE FROM tempature_history")
    void deleteAll();  // 删除表中所有数据

    @Query("DELETE FROM tempature_history WHERE timestamp < :timestamp")
    void deleteOlderThan(String timestamp);  // 删除表中5天前的数据
}
