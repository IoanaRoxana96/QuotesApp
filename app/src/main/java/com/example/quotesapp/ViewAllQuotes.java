package com.example.quotesapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewAllQuotes extends AppCompatActivity {

    DatabaseHelper db;
    ArrayList<Quote> quotesList;
    Quote quote;
    ListView listView;

    private ActionMode mActionMode;
    private CheckBox checkBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewallquotes_layout);

        checkBox = (CheckBox) findViewById(R.id.checkbox_item);
       /* checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    mActionMode = ViewAllQuotes.this.startActivity(new ActionBarCallBack());
                else
                    mActionMode.finish();
            }
        });*/

        db  = new DatabaseHelper(this);
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
            TwoColumn_ListAdapter adapter = new TwoColumn_ListAdapter(this, R.layout.list_adapter_view, quotesList);
            listView = (ListView) findViewById(R.id.listViewAll);
            listView.setAdapter(adapter);
        }
    }
/*
    class ActionBarCallBack implements ActionMode.Callback {
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.contextual_menu, menu);
            return true;
        }
    }*/

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

/*
    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    @Override
    public boolean onPrepareActionMode (ActionMode mode, Menu menu) {
        mode.setTitle("CheckBox is checked");
        return false;
    }
*/
}
