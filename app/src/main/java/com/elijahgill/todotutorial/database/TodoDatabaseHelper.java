package com.elijahgill.todotutorial.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Elijah on 1/10/2015.
 */
public class TodoDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todotable.db";
    private static final int DATABASE_VERSION = 2;

    public TodoDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Call the database methods when we need to create
    // Or upgrade them
    @Override
    public void onCreate(SQLiteDatabase databse){
        TodoTable.onCreate(databse);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
        int newVersion){
        TodoTable.onUpgrade(database,oldVersion,newVersion);
    }
}
