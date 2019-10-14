package com.example.quotesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ThreeColumn_ListAdapter extends ArrayAdapter<QuoteTop> {
    private LayoutInflater mInflater;
    private ArrayList<QuoteTop> quotes;
    private int mViewResourceId;

    public ThreeColumn_ListAdapter(Context context, int textViewResourceId, ArrayList<QuoteTop> quotes) {
        super(context, textViewResourceId, quotes);
        this.quotes = quotes;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parents) {
        convertView = mInflater.inflate(mViewResourceId, null);

        QuoteTop quote = quotes.get(position);
        if (quote != null) {
            TextView id = (TextView) convertView.findViewById(R.id.textId);
            TextView quot = (TextView) convertView.findViewById(R.id.textQuote);
            TextView n_of_occ = (TextView) convertView.findViewById(R.id.textN_of_occ);

            if (id != null) {
                id.setText(quote.getId());
            }

            if (quot != null) {
                quot.setText(quote.getQuote());
            }

            n_of_occ.setText(quote.getN_Of_Occ());
        }
        return convertView;
    }
}
