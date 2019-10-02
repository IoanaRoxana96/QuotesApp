package com.example.quotesapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Quotes.db";
    public static final String TABLE_NAME = "quotes_table";
    public static final String TABLE_NAME2 = "quotes_table2";

    public static final String COL_1 = "ID";
    public static final String COL_2 = "QUOTE";
    public static final String COL_12 = "ID2";
    public static final String COL_22 = "QUOTE2";
    public static final String COL_32 = "N_OF_OCC";






    public DatabaseHelper (Context context) {
        super(context, DATABASE_NAME, null, 21);
        //context.deleteDatabase("Quotes.db");
        //SQLiteDatabase db = this.getWritableDatabase();
    }



    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," +  COL_2  + " TEXT NOT NULL);");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME2 + " (" + COL_12 + " INTEGER PRIMARY KEY AUTOINCREMENT, "+  COL_22  + " TEXT NOT NULL," + COL_32 + " INTEGER);");
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);

        onCreate(db);
    }



    public boolean insertQuote(String quote) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, quote);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;

    }



    public boolean insertQuote2(String quote) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_22, quote);
        contentValues.put(COL_32, 0);
        long result = db.insert(TABLE_NAME2, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;

    }

    public boolean checkQuote (String quote) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT QUOTE FROM " + TABLE_NAME + " WHERE QUOTE =? " + " COLLATE NOCASE ", new  String[] {quote});
        if(cursor.getCount() > 0)
            return true;
        return false;
    }



    public boolean checkRandom(String quote) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(" UPDATE " + TABLE_NAME2 + " SET " + COL_32 + " = N_OF_OCC + 1 " + " WHERE QUOTE2 = ? " , new String[]{quote});
        if (cursor.getCount() > 0 )
            return true;
        return false;
    }



    public Cursor getAllQuotes() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + COL_1 + ", " +  COL_2  + " FROM " + TABLE_NAME, null);
        // Cursor res = db.rawQuery(" SELECT * FROM " + TABLE_NAME, null);
        return res;
    }


    public Cursor getTop() {
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor res1 = db.rawQuery("SELECT " + COL_22 + ", COUNT (*) AS N_Of_Occ FROM " + TABLE_NAME2 + " GROUP BY " + COL_22 + " ORDER BY N_Of_Occ  DESC ", null);
        Cursor res1 = db.rawQuery("SELECT * FROM " + TABLE_NAME2, null);
        return res1;
    }


    public Cursor getRandomQuote() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY RANDOM() LIMIT 1", null);
        return res;

    }


    public Integer deleteQuote(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {id});
    }


}
