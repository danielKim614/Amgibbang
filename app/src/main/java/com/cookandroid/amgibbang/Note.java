package com.cookandroid.amgibbang;

public class Note {
    int _id;
    String todo;
    int checkBox;

    public Note(int _id, String todo, int checkBox){
        this._id = _id;
        this.todo = todo;
        this.checkBox = checkBox;
    }

    public int get_id() { return _id; }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public int getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(int checkBox) {
        this.checkBox = checkBox;
    }
}