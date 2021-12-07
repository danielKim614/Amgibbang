package com.cookandroid.amgibbang;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NoteDatabase {
    private static NoteDatabase database;
    public static String DATABASE_NAME = "todo.db";
    public static String TABLE_NOTE = "NOTE";
    public static int DATABASE_VERSION = 1;
    private DatabaseHelper dbHelper;

    private Context context;
    private SQLiteDatabase db;

    private NoteDatabase(Context context){
        this.context = context;
    }

    public static NoteDatabase getInstance(Context context){
        if(database == null){
            database = new NoteDatabase(context);
        }

        return database;
    }

    public Cursor rawQuery(String SQL){

        Cursor c1 = null;
        try{
            c1 = db.rawQuery(SQL,null);
        } catch (Exception ex){
            Log.e("DB","Exception in rawQuery",ex);
        }

        return c1;
    }

    public boolean execSQL(String SQL) {

        try {
            Log.d("DB", "SQL : " + SQL);
            db.execSQL(SQL);
        } catch(Exception ex) {
            Log.e("DB", "Exception in execSQL", ex);
            return false;
        }
        return true;
    }

    public boolean open(){

        dbHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = dbHelper.getWritableDatabase();

        return true;
    }

    public void close(){
        db.close();
        database = null;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
            super(context, name,factory,version);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {

            String DROP_SQL = "drop table if exists " +TABLE_NOTE;

            try {
                db.execSQL(DROP_SQL);

            } catch (Exception ex){
                Log.e("DB", "Exception in DROP_SQL", ex);
            }

            String CREATE_SQL = "create table " + TABLE_NOTE + "("
                    + " _id integer NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + "  TODO TEXT DEFAULT '', "
                    + " checkBox integer NOT NULL DEFAULT 0"
                    + ")";
            try{
                db.execSQL(CREATE_SQL);
            } catch (Exception ex){
                Log.e("DB","Exception in CREATE_SQL", ex);
            }

            String CREATE_INDEX_SQL = "create index " + TABLE_NOTE + "_IDX ON " + TABLE_NOTE + "("
                    + "_id"
                    + ")";
            try{
                db.execSQL(CREATE_INDEX_SQL);
            } catch (Exception ex){
                Log.e("DB", "Exception in CREATE_INDEX_SQL",ex);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
