package com.example.yperinterntest;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

//데이터 모델
@Entity(tableName = "items")
public class RecyclerItem {
    @PrimaryKey(autoGenerate = true)
    private Integer key;

    private String address;
    private String time;

    //생성자
    public RecyclerItem(){
    }

    public RecyclerItem(Integer key, String address, String time) {
        this.key = key;
        this.address = address;
        this.time = time;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {

        this.time = time;
    }

    public void setAddressTime(String address, String time){
        this.address = address;
        this.time = time;
    }

}