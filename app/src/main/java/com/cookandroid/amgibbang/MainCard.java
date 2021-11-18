package com.cookandroid.amgibbang;

import android.widget.CheckBox;

import java.util.ArrayList;

public class MainCard {
    private String name;
    private boolean isBookmark = false;
    private CheckBox checkBox;

    public MainCard(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public boolean isBookmark() {
        return isBookmark;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }
}
