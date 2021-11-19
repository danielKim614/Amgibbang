package com.cookandroid.amgibbang;

import android.widget.CheckBox;

import java.util.ArrayList;

public class MainCard {
    private boolean checkBox;
    private boolean bookmark;
    private String name;

    public MainCard() {}

    public MainCard(String name, boolean isBookmark, boolean checkBox){
        this.name=name;
        this.bookmark=isBookmark;
        this.checkBox=checkBox;
    }

    public boolean getBookmark() {
        return bookmark;
    }
    public boolean getCheckBox() {
        return checkBox;
    }
    public String getName() {
        return name;
    }
    public void cancelName(String name){
        this.name=name;
    }


}
