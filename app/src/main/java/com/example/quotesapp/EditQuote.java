package com.example.quotesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditQuote extends ViewAllQuotes {
    DatabaseHelper db;
    EditText editQuoteTxt;
    Button saveBtn;
    Button viewAllQuotesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_quote);

        db = new DatabaseHelper(this);
        editQuoteTxt = findViewById(R.id.editQuote);
        saveBtn = findViewById(R.id.button_save);
        viewAllQuotesBtn = findViewById(R.id.button_view);

        editQuoteToDB();
        viewAllQuotesFromDB();
    }

    public void editQuoteToDB() {
        saveBtn.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                boolean isUpdate = db.editQuote(String.valueOf(editQuoteTxt.getText()));
                                if (isUpdate == true)
                                    Toast.makeText(getBaseContext(), "Quote edited!", Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(getBaseContext(), "Quote wasn't edited!", Toast.LENGTH_LONG).show();

                            }

                });
    }

    public void viewAllQuotesFromDB() {
        viewAllQuotesBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(EditQuote.this, ViewAllQuotes.class);
                        startActivity(intent);
                    }
                });
    }

    public static void start(Context ctx) {
        Intent intent = new Intent(ctx, AddQuote.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ctx.startActivity(intent);

    }

}

