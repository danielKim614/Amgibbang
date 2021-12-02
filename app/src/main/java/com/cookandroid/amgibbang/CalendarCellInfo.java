package com.cookandroid.amgibbang;

public class CalendarCellInfo {
    int level;   // 1~4 중 하나. 셀 색깔 어떤 걸로 할 지 나타내는..
    int score;
    int total;

    public CalendarCellInfo() { }

    public CalendarCellInfo(int level, int score, int total) {
        this.score = score;
        this.total = total;

        double score_total = ((double) score) / total;
        if (score_total < 0.25) level = 4;
        else if (score_total < 0.5) level = 3;
        else if (score_total < 0.75) level = 2;
        else level = 1;
    }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }

    public int getTotal() {
        return total;
    }
}