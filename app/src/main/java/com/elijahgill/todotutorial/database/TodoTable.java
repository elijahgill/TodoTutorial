package com.elijahgill.todotutorial.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Elijah on 1/10/2015.
 */
public class TodoTable {

    // table names
    public static final String TABLE_TODO = "todo";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_SUMMARY = "summary";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IS_DONE = "is_done";
    public static final String COLUMN_DUE_DATE = "due_date";
    public static final String COLUMN_DATE_CREATED = "date_created";

    // Creation SQL
    private static final String DATABASE_CREATE = "create table "
            +TABLE_TODO
            +"("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_CATEGORY + " text not null, "
            + COLUMN_SUMMARY + " text not null, "
            + COLUMN_DESCRIPTION + " text not null, "
            + COLUMN_IS_DONE + " integer, "
            + COLUMN_DUE_DATE + " text, "
            + COLUMN_DATE_CREATED + " text"
            +");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        Log.w(TodoTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                +", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        onCreate(database);
    }
}
