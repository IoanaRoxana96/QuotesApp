package com.example.quotesapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewTopQuotes extends AppCompatActivity {
    DatabaseHelper db;
    ArrayList<Quote> quotesList;
    ListView listView;
    Quote quote;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewtopquotes_layout);

        db  = new DatabaseHelper(this);

        quotesList = new ArrayList<>();
        Cursor res = db.getTop();
        int numRows = res.getCount();
        if (numRows == 0) {
            Toast.makeText(ViewTopQuotes.this, "There is nothing in the database!", Toast.LENGTH_LONG).show();
        } else {
            while (res.moveToNext()) {
                quote = new Quote(res.getString(0), res.getString(1), res.getString(2));
                quotesList.add(quote);
            }
            ThreeColumn_ListAdapter adapter = new ThreeColumn_ListAdapter(this, R.layout.list_adapter_view_top, quotesList);
            listView = (ListView) findViewById(R.id.listViewTop);
            listView.setAdapter(adapter);
        }
    }
}
