package com.cookandroid.amgibbang;

import android.widget.CheckBox;

import java.util.ArrayList;

public class MainCard {
    private String name;
    private boolean isBookmark = false;
    private boolean checkBox;

    public MainCard(String name, boolean isBookmark, boolean checkBox){
        this.name=name;
        this.isBookmark=isBookmark;
        this.checkBox=checkBox;
    }

    public String getName() {
        return name;
    }

    public boolean isBookmark() {
        return isBookmark;
    }

    public boolean getCheckBox() {
        return checkBox;
    }
}
