package com.ayush.wikisearch.model;


import com.google.gson.annotations.SerializedName;

/**
 * Created by ayushkedia on 03/06/16.
 */
public class SearchSuggestion {

    public Pages query;

    @SerializedName("continue")
    public Continue _continue;

    public ErrorObject error;
}
