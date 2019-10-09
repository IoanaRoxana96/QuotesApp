package com.example.quotesapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewAllQuotes extends AppCompatActivity {

    DatabaseHelper db;
    ArrayList<Quote> quotesList;
    Quote quote;
    ListView listView;
    ListViewAdapter adapter;
    private final String fromColumnArr[] = {db.id, db.quotes};
    private final int toViewIdArr[] = {R.id.textId, R.id.textQuote};
    private List<Quote> quoteCheckedItemList = new ArrayList<Quote>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewallquotes_layout);

        getQuotes();

        ListViewAdapter adapter = new ListViewAdapter(this, R.layout.list_adapter_view, quotesList);
        listView = (ListView) findViewById(R.id.listViewAll);
        listView.setAdapter(adapter);
    }

    private String getQuoteCheckedItemIds() {
        StringBuffer retBuf = new StringBuffer();
        if (quoteCheckedItemList != null) {
            int size = quoteCheckedItemList.size();
            for (int i = 0; i < size; i++) {
                Quote tmp = quoteCheckedItemList.get(i);
                retBuf.append(tmp.getId());
                retBuf.append(" ");
            }
        }
        return retBuf.toString().trim();
    }


    private void getQuotes() {
        db = new DatabaseHelper(this);
        quotesList = new ArrayList<>();
        Cursor res = db.getAllQuotes();
        int numRows = res.getCount();
        if (numRows == 0) {
            Toast.makeText(ViewAllQuotes.this, "There is nothing in the database!", Toast.LENGTH_LONG).show();
        } else {
            while (res.moveToNext()) {
                quote = new Quote(res.getString(0), res.getString(1), null);
                quotesList.add(quote);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent = new Intent(this, AddQuote.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_edit) {
            Intent intent = new Intent(this, EditQuote.class);
            startActivity(intent);
        } else if (id == R.id.action_delete) {
            Cursor res = db.getAllQuotes();
            Integer deleteRows = db.deleteQuote(res.getString(0));
            if (deleteRows > 0)
                Toast.makeText(ViewAllQuotes.this, "Quote deleted!", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(ViewAllQuotes.this, "Quote already deleted or id doesn't exist!", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
        }
    }

