package com.example.viltsu.birdbonging.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.viltsu.birdbonging.data.BirdContract.BirdEntry;

/**
 * Created by ville-pekkapalmgren on 25/01/18.
 */

public class BirdHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG ="BirdHelper.class.getSimpleName";
    public static final int DATABASE_VERSION =3;
    public static final String DATABASE_NAME="BirdBonger.db";

    public BirdHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String SQL_CREATE_BIRDS_TABLE = "CREATE TABLE " + BirdEntry.TABLE_NAME + " ("
                + BirdEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BirdEntry.COLUMN_BIRD_BREED + " TEXT NOT NULL, "
                + BirdEntry.COLUMN_BIRD_POINTS + " TEXT NOT NULL, "
                + BirdEntry.COLUMN_BIRD_PIC + " INTEGER, "
                + BirdEntry.COLUMN_BIRD_MESSAGE + " TEXT);";
        db.execSQL(SQL_CREATE_BIRDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
