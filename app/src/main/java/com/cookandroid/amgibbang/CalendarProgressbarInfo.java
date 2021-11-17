package com.cookandroid.amgibbang;

public class CalendarProgressbarInfo {
    int progress;
    int score;
    int total;

    CalendarProgressbarInfo() {}
    CalendarProgressbarInfo(int progress, int total, int score) {
        this.progress = progress;
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
