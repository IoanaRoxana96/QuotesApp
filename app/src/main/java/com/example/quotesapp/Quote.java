package com.example.quotesapp;

public class Quote {

    private String id;
    private String quote;

    public Quote(String quote){
        this.quote = quote;
    }

    public Quote() {

    }
    public void setId(String  id) {
        this.id = id;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }



    public Quote(String id, String quote) {
        this.id = id;
        this.quote = quote;
    }

    public String getId() {
        return id;
    }

    public String getQuote() {
        return quote;
    }


}
