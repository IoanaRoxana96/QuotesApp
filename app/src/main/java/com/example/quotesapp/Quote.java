package com.example.quotesapp;

public class Quote {

    private String Id;

    private String Quote;
    private String N_Of_Occ;

    public void setId(String id) {
        Id = id;
    }

    public void setQuote(String quote) {
        Quote = quote;
    }

    public void setN_Of_Occ(String n_Of_Occ) {
        N_Of_Occ = n_Of_Occ;
    }


    public Quote(String id, String quote, String n_Of_Occ) {
        Id = id;
        Quote = quote;
        N_Of_Occ = n_Of_Occ;
    }

    public String getId() {
        return Id;
    }

    public String getQuote() {
        return Quote;
    }
    public String getN_Of_Occ() {
        return N_Of_Occ;
    }


}
