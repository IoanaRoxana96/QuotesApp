package com.example.quotesapp;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CheckIntegrity extends AppCompatActivity {
    DatabaseHelper db;
    Button checkBtn;
    Button createCheckSumBtn;
    Button createInitialCheckSumBtn;
    String filePath = "/data/data/com.example.quotesapp/databases/QuotesDB.db";
    String OriginalHex = null;
    String NewHex = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_integrity);

        db = new DatabaseHelper(this);
        checkBtn = findViewById(R.id.verify_button);
        createCheckSumBtn = findViewById(R.id.checksum_button);
        createInitialCheckSumBtn = findViewById(R.id.initialchecksum_button);

        check();
        createInitialCheckSum();
        createNewCheckSum();
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
                        Toast.makeText(CheckIntegrity.this, "Initial checksum generated", Toast.LENGTH_SHORT).show();
                        Log.d("Valoare hex initial", OriginalHex);
                    }
                });
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
                        Toast.makeText(CheckIntegrity.this, "New checksum generated", Toast.LENGTH_SHORT).show();
                        Log.d("Valoare hex nou", NewHex);
                    }
                });
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
                        if (OriginalHex != null && NewHex != null & OriginalHex.equals(NewHex))
                            Toast.makeText(CheckIntegrity.this, "Database is not corrupted", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(CheckIntegrity.this, "Database corrupted", Toast.LENGTH_SHORT).show();
                        //Log.d("Verificare hex original", OriginalHex);
                        //Log.d("Verificare hex nou", NewHex);
                    }
                });
    }

}
