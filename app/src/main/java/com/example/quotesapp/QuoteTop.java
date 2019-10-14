package com.example.quotesapp;

public class QuoteTop {
    private String id;
    private String quote;
    private String n_Of_Occ;

    public QuoteTop(String quote){
        this.quote = quote;
    }

    public QuoteTop() {

    }
    public void setId(String  id) {
        this.id = id;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public void setN_Of_Occ(String n_Of_Occ) {
        this.n_Of_Occ = n_Of_Occ;
    }


    public QuoteTop(String id, String quote, String n_Of_Occ) {
        this.id = id;
        this.quote = quote;
        this.n_Of_Occ = n_Of_Occ;
    }

    public String getId() {
        return id;
    }

    public String getQuote() {
        return quote;
    }
    public String getN_Of_Occ() {
        return n_Of_Occ;
    }
}
