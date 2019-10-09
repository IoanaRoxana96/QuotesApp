package com.example.quotesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ListViewAdapter extends ArrayAdapter<Quote> {
    private LayoutInflater mInflater;
    private ArrayList<Quote> quotes;
    private int mViewResourceId;
    DatabaseHelper db;

    public ListViewAdapter(Context context, int textViewResourceId, ArrayList<Quote> quotes) {
        super(context, textViewResourceId, quotes);
        this.quotes = quotes;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parents) {
        convertView = mInflater.inflate(mViewResourceId, null);
        CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.checkbox_item);
        Quote quote = quotes.get(position);
        if (quote != null) {
            final TextView id = (TextView) convertView.findViewById(R.id.textId);
            final TextView quot = (TextView) convertView.findViewById(R.id.textQuote);

            if (id != null) {
                id.setText(quote.getId());
            }

            if (quot != null) {
                quot.setText(quote.getQuote());
            }
        }
        return convertView;
    }
}
