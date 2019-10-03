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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends AppCompatActivity {

    DatabaseHelper db;
    Button viewAllQuotesBtn;
    Button topQuoteBtn;
    Button requestBtn;
    Button checkBtn;
    TextView showOutput;
    ProgressDialog progressDialog;

    private static String file_url = "http://quotes.rest/qod.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        viewAllQuotesBtn = findViewById(R.id.button_view);
        topQuoteBtn = findViewById(R.id.button_top);
        requestBtn = findViewById(R.id.request_button);
        checkBtn = findViewById(R.id.check_button);
        showOutput = findViewById(R.id.showOutput);

        viewAllQuotesFromDB();
        randomQuote();
        topQuotes();
        check();

        progressDialog = new ProgressDialog(this);
        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask().execute(file_url);

            }
        });
    }

    public void check() {
        checkBtn.setOnClickListener(
                new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View view) {
                        openCheckIntegrity();
                    }
                });
    }

    public void openCheckIntegrity() {
        Intent intent = new Intent(this, CheckIntegrity.class);
        startActivity(intent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void topQuotes() {
        topQuoteBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, ViewTopQuotes.class);
                        startActivity(intent);
                    }
                });
    }

    public void randomQuote() {
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

    public void viewAllQuotesFromDB() {
        viewAllQuotesBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, ViewAllQuotes.class);
                        startActivity(intent);
                    }
                });
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