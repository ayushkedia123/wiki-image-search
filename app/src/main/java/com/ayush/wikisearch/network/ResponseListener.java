package com.ayush.wikisearch.network;

import com.ayush.wikisearch.model.ErrorObject;

/**
 * Created by ayushkedia on 03/06/16.
 */
public interface ResponseListener {
    void onResponse(Object pResponse);
    void onError(ErrorObject error);
}
