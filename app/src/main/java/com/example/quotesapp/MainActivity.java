package com.example.quotesapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText addQuoteTxt, addQuoteId;
    Button addQuoteBtn;
    Button deleteQuoteBtn;
    Button viewAllQuotesBtn;
    Button randomQuoteBtn;
    Button topQuoteBtn;
    Button requestBtn;
    Button checkBtn;
    Button createCheckSumBtn;
    Button createInitialCheckSumBtn;
    TextView showOutput;
    ProgressDialog progressDialog;
    String filePath = "/data/data/com.example.quotesapp/databases/QuotesDB.db";
    String OriginalHex = null;
    String NewHex = null;

    private static String file_url = "http://quotes.rest/qod.json";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        addQuoteTxt = findViewById(R.id.editQuote);
        addQuoteId = findViewById(R.id.editQuoteId);
        addQuoteBtn = findViewById(R.id.button_add);
        deleteQuoteBtn = findViewById(R.id.button_delete);
        viewAllQuotesBtn = findViewById(R.id.button_view);
        randomQuoteBtn = findViewById(R.id.button_random);
        topQuoteBtn = findViewById(R.id.button_top);
        requestBtn = findViewById(R.id.request_button);
        checkBtn = findViewById(R.id.verify_button);
        createCheckSumBtn = findViewById(R.id.checksum_button);
        createInitialCheckSumBtn = findViewById(R.id.initialchecksum_button);
        showOutput = findViewById(R.id.showOutput);

        addQuoteToDB();
        deleteQuoteFromDB();
        viewAllQuotesFromDB();
        randomQuote();
        topQuotes();
        check();
        createInitialCheckSum();
        createNewCheckSum();

        progressDialog = new ProgressDialog(this);
        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask().execute(file_url);

            }
        });
    }

    public void createInitialCheckSum() {
        createInitialCheckSumBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MessageDigest md = null;
                        try {
                            md = MessageDigest.getInstance("SHA-256");
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        OriginalHex = checksum (filePath, md);
                        Toast.makeText(MainActivity.this, "Initial checksum generated", Toast.LENGTH_SHORT).show();
                        Log.d("Valoare hex initial", OriginalHex);
                    }
                }
        );
    }

    public void createNewCheckSum() {
        createCheckSumBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MessageDigest md = null;
                        try {
                            md = MessageDigest.getInstance("SHA-256");
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        NewHex = checksum (filePath, md);
                        Toast.makeText(MainActivity.this, "New checksum generated", Toast.LENGTH_SHORT).show();
                        Log.d("Valoare hex nou", NewHex);
                    }
                }
        );
    }

    private static String checksum(String filepath, MessageDigest md) {
        try {
            DigestInputStream dis = new DigestInputStream(new FileInputStream(filepath), md);
            while (dis.read() != -1)
                md = dis.getMessageDigest();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder result = new StringBuilder();
        for (byte b : md.digest()) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    public void check() {
        checkBtn.setOnClickListener(
                new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View view) {
                        if (OriginalHex.equals(NewHex))
                            Toast.makeText(MainActivity.this, "Database is not corrupted", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(MainActivity.this, "Database corrupted", Toast.LENGTH_SHORT).show();
                        //Log.d("Verificare hex original", OriginalHex);
                        //Log.d("Verificare hex nou", NewHex);
                    }
                }
        );
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /*public void topQuotes() {
        topQuoteBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cursor res1 = db.getTop();
                        if (res1.getCount() == 0) {
                            showMessage("Error!!!", "Nothing found!");
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while (res1.moveToNext()) {
                            buffer.append("Id: " + res1.getString(0) + "\n");
                            buffer.append("Quote: " + res1.getString(1) + "\n");
                            buffer.append("N_of_occ: " + res1.getString(2) + "\n\n");
                        }
                        showMessage("Top random quotes:", buffer.toString());
                    }
                });
    }*/

    public void topQuotes() {
        topQuoteBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, ViewTopQuotes.class);
                        startActivity(intent);
                    }
                }
        );
    }

    public void deleteQuoteFromDB() {
        deleteQuoteBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Integer deleteRows = db.deleteQuote(addQuoteId.getText().toString());
                        if (deleteRows > 0)
                            Toast.makeText(MainActivity.this, "Quote deleted!", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this, "Quote already deleted or id doesn't exist!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void randomQuote() {
        randomQuoteBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String stringForQuote = null;
                        TextView rQuote = (TextView) findViewById(R.id.randomQuote);
                        Cursor res = db.getRandomQuote();
                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            //buffer.append("Quote: " + res.getString(1) + "\n\n");
                            stringForQuote = res.getString(1);
                            rQuote.setText(stringForQuote);
                            db.checkRandom(stringForQuote);
                        }
                    }
                });
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
                                // myDb.insertQuote2(editQuote.getText().toString());
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
                        Intent intent = new Intent(MainActivity.this, ViewAllQuotes.class);
                        startActivity(intent);
                    }
                }
        );
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


    public class JSONTask extends AsyncTask<String, Integer, String> {
        BufferedReader reader = null;
        String resultJson;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Progress bar");
            progressDialog.setMessage("Getting quote of the day...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            int count;
            publishProgress(0);
            try {
                //Log.d("Verificare", "Intru in doInBackground");
                URL url = new URL(params[0]);
                URLConnection connection = url.openConnection();
                int lengthOfFile = connection.toString().length();
                //Log.d("Verificare", "Lungime content   " + lengthOfFile);
                InputStream input = url.openStream();

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress((int) ((total * 100) / lengthOfFile));
                }

                input.close();

                StringBuffer buffer = new StringBuffer();
                String line = "";
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                reader.close();

                resultJson = buffer.toString();
                JSONObject parentObject = new JSONObject(resultJson);
                JSONObject object = parentObject.getJSONObject("contents");

                JSONArray parentArray = object.getJSONArray("quotes");
                JSONObject finalObject = parentArray.getJSONObject(0);

                String dailyQuote = finalObject.getString("quote");

                return dailyQuote;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                System.err.println("Malformed URL encountered: " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("An IOException was caught: " + e.getMessage());
            }  catch (JSONException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("An IOException was caught: " + e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            progressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            showOutput.setText(result);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Too many requests for this page. Please wait 1 hour before send a new request! ");
            if (isNetworkAvailable() == true) {
                if (result == null) {
                    alertDialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    alertDialog.show();
                } else {
                    if (db.checkQuote(showOutput.getText().toString()) == true) {
                        Toast.makeText(getBaseContext(), "Quote already exist in the database! Add another one tomorrow.", Toast.LENGTH_LONG).show();
                    } else {
                        db.insertQuote(showOutput.getText().toString());
                        //myDb.insertQuote2(showOutput.getText().toString());
                        Toast.makeText(getBaseContext(), "Completed." + "\n" + "Quote inserted!", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(getBaseContext(),"No internet connection! Please connect if you want to see the quote of the day!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}