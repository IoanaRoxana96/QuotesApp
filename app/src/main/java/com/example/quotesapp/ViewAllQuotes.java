package com.example.quotesapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.CamcorderProfile;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class ViewAllQuotes extends AppCompatActivity {
    DatabaseHelper db;
    ArrayList<Quote> allQuotesList;
    ListView listView;
    Quote quote;
    TwoColumnListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_quotes);

        db = new DatabaseHelper(this);

        allQuotesList = new ArrayList<>();
        Cursor res = db.getAllQuotes();
        int numRows = res.getCount();
        while (res.moveToNext()) {
            quote = new Quote(res.getString(0), res.getString(1));
            allQuotesList.add(quote);
        }
        adapter = new TwoColumnListAdapter(this, R.layout.list_view_adapter, allQuotesList);
        listView = (ListView) findViewById(R.id.listViewAll);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (numRows == 0) {
            Toast.makeText(ViewAllQuotes.this, "There is nothing in the database!", Toast.LENGTH_LONG).show();
        }

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                final CharSequence[] items = {"Edit", "Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(ViewAllQuotes.this);
                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            Cursor c = db.getAllQuotes();
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            showDialogUpdate(ViewAllQuotes.this, arrID.get(position));
                        } if (i == 1) {
                            Cursor c = db.getAllQuotes();
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });

    }
    private void showDialogDelete(final int idQuote) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(ViewAllQuotes.this);
        dialogDelete.setTitle("Warning!");
        dialogDelete.setMessage("Are you sure do you want to delete this quote?");
        dialogDelete.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    db.deleteQuote(idQuote);
                    Toast.makeText(ViewAllQuotes.this, "Delete successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
            }
        });

        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void showDialogUpdate(Activity activity, final int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_dialog);
        dialog.setTitle("Update");

        final EditText editQuote = dialog.findViewById(R.id.editQuote);
        Button updateButton = dialog.findViewById(R.id.update);

        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels*0.95);
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels*0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    db.updateQuote(position, editQuote.getText().toString().trim());
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Update successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
                //updateQuoteList();
            }
        });
    }

    /*private void updateQuoteList() {
        Cursor cursor = db.getAllQuotes();
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String quote = cursor.getString(1);
            allQuotesList.add(new Quote(id, quote));

        }
    }*/

}
