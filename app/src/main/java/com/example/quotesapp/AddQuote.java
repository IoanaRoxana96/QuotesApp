package com.example.quotesapp;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddQuote extends ViewAllQuotes {
    DatabaseHelper db;
    EditText addQuoteTxt;
    Button addQuoteBtn;
    Button viewAllQuotesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        addQuoteTxt = findViewById(R.id.editQuote);
        addQuoteBtn = findViewById(R.id.button_add);
        viewAllQuotesBtn = findViewById(R.id.button_view);

        addQuoteToDB();
        viewAllQuotesFromDB();
    }

    public void addQuoteToDB() {
        addQuoteBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean quoteExists = db.checkQuote(addQuoteTxt.getText().toString());
                        if ((addQuoteTxt.length()) != 0) {
                            if (quoteExists == true) {
                                Toast.makeText(getBaseContext(), "Quote already exist! Please add another one.", Toast.LENGTH_LONG).show();
                            } else {
                                db.insertQuote(addQuoteTxt.getText().toString());
                                Toast.makeText(getBaseContext(), "Quote inserted!", Toast.LENGTH_LONG).show();
                            }
                            addQuoteTxt.setText("");
                        } else
                            Toast.makeText(getBaseContext(), "You must write a quote in the text field!", Toast.LENGTH_LONG).show();
                    }

                });
    }

    public void viewAllQuotesFromDB() {
        viewAllQuotesBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AddQuote.this, ViewAllQuotes.class);
                        startActivity(intent);
                    }
                });
    }




}
