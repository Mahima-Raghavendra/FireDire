package com.example.firedire;

public class DataList {
    public String date,time;
    public long temperature;
    static long count = 0;
    public DataList(){
        count++;
    }
    public long getCount(){
        return count;
    }
}
