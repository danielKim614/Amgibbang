package com.cookandroid.amgibbang;

import java.io.Serializable;

public class Time implements Serializable {
    String nameT;
    String startTime;
    String endTime;

    Time() {}

    Time(String name, String startTime, String endTime) {
        this.nameT = name;
        this.startTime = startTime;
        this.endTime = endTime;

    }

    public String getNameT(){return nameT;}

    public String getStartTime(){return startTime;}

    public String getEndTime(){return endTime;}
}
