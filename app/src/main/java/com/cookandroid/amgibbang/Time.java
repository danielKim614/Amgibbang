package com.cookandroid.amgibbang;

import java.io.Serializable;

public class Time implements Serializable {
    String name;
    int hour;
    int minute;
    int second;

    Time() {}

    Time(String name, int hour, int minute, int second) {
        this.name = name;
        this.hour = hour;
        this.minute = minute;
        this.second = second;

    }

    public String getName(){return name;}

    public int getHour(){return hour;}

    public int getMinute(){return minute;}

    public int getSecond(){return second;}

}
