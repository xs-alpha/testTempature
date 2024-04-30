package com.xiaosheng.testtempature.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tempature")
public class Tempature {
    @PrimaryKey
    private Integer id;
    private String json;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
