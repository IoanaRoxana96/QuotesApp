package com.example.quotesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Editable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_QUOTES = "QuotesDB.db";
    public static final String TABLE_QUOTES = "quotes_tbl";

    public static final String id = "ID";
    public static final String quotes = "QUOTE";
    public static final String n_of_occ = "N_OF_OCC";

    public DatabaseHelper (Context context) {
        super(context, DATABASE_QUOTES, null, 3);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_QUOTES + " (" + id + " INTEGER PRIMARY KEY AUTOINCREMENT," +  quotes  + " TEXT NOT NULL," + n_of_occ + " INTEGER);");
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUOTES);
        onCreate(db);
    }

    public boolean insertQuote(String quote) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(quotes, quote);
        contentValues.put(n_of_occ, 0);
        long result = db.insert(TABLE_QUOTES, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean editQuote (String quote) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(quotes, quote);
        db.update(TABLE_QUOTES, contentValues, " QUOTE = ? ", new String[]{quote} );
        return true;
    }

    public boolean checkQuote (String quote) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT QUOTE FROM " + TABLE_QUOTES + " WHERE QUOTE =? " + " COLLATE NOCASE ", new  String[] {quote});
        if(cursor.getCount() > 0)
            return true;
        return false;
    }

    public boolean checkRandom(String quote) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(" UPDATE " + TABLE_QUOTES + " SET " + n_of_occ + " = N_OF_OCC + 1 " + " WHERE QUOTE = ? " , new String[]{quote});
        if (cursor.getCount() > 0 )
            return true;
        return false;
    }

    public Cursor getAllQuotes() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + id + ", " +  quotes  + " FROM " + TABLE_QUOTES, null);
        return res;
    }


    public Cursor getTop() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res1 = db.rawQuery("SELECT * FROM " + TABLE_QUOTES, null);
        return res1;
    }

    public Cursor getRandomQuote() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_QUOTES + " ORDER BY RANDOM() LIMIT 1", null);
        return res;
    }

    public Integer deleteQuote(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_QUOTES, "ID = ?", new String[] {String.valueOf(id)});
    }

}
