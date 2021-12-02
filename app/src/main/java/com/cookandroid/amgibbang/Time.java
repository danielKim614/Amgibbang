package com.cookandroid.amgibbang;

import java.io.Serializable;
import java.time.LocalTime;

public class Time implements Serializable {
    String name;
    LocalTime startTime;
    LocalTime endTime;

    Time() {}

    Time(String name, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;

    }

    public String getNameT(){return name;}

    public LocalTime getStartTime(){return startTime;}

    public LocalTime getEndTime(){return endTime;}
}
