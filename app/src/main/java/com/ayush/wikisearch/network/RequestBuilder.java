package com.ayush.wikisearch.network;

import com.ayush.wikisearch.model.SearchSuggestion;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by ayushkedia on 03/06/16.
 */
public interface RequestBuilder {

    @GET("/")
    void getSearchSuggestion(@Query("action") String action,
                             @Query("prop") String prop,
                             @Query("format") String format,
                             @Query("piprop") String piprop,
                             @Query("pithumbsize") String pithumbsize,
                             @Query("pilimit") String pilimit,
                             @Query("generator") String generator,
                             @Query("gpssearch") String gpssearch,
                             @Query("gpsoffset") String gpsoffset,
                             Callback<SearchSuggestion> response);

}
