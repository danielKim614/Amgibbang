package com.cookandroid.amgibbang;

import java.io.Serializable;

public class CalendarProgressbarInfo implements Serializable {
    String title;
    int progress;
    int score;
    int total;

    CalendarProgressbarInfo() {}

    CalendarProgressbarInfo(String title, int score, int total) {
        this.title = title;
        this.progress = (int)(((double)score / total) * 100);
        this.score = score;
        this.total = total;
    }

    CalendarProgressbarInfo(int score, int total) {
        this.progress = (int)(((double)score / total) * 100);
        this.score = score;
        this.total = total;
    }

    public int getProgress() {
        return progress;
    }

    public int getScore() {
        return score;
    }

    public int getTotal() {
        return total;
    }
}
