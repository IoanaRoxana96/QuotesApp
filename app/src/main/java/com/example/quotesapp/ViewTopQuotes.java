package com.example.quotesapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewTopQuotes extends AppCompatActivity {
    DatabaseHelper db;
    ArrayList<QuoteTop> quotesList;
    ListView listView;
    QuoteTop quote;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewtopquotes_layout);

        db = new DatabaseHelper(this);

        quotesList = new ArrayList<>();
        Cursor res = db.getTop();
        int numRows = res.getCount();
        if (numRows == 0) {
            Toast.makeText(ViewTopQuotes.this, "There is nothing in the database!", Toast.LENGTH_LONG).show();
        } else {
            while (res.moveToNext()) {
                quote = new QuoteTop(res.getString(0), res.getString(1), res.getString(2));
                quotesList.add(quote);
            }
            ThreeColumn_ListAdapter adapter = new ThreeColumn_ListAdapter(this, R.layout.list_adapter_view_top, quotesList);
            listView = (ListView) findViewById(R.id.listViewTop);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent = new Intent(this, AddQuote.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_back) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
