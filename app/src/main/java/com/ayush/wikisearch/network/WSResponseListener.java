package com.ayush.wikisearch.network;

import android.content.Context;

import com.ayush.wikisearch.model.ApiErrorResponse;
import com.ayush.wikisearch.model.ErrorObject;

import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ayushkedia on 03/06/16.
 */

public class WSResponseListener<T> implements Callback<T> {
    private WeakReference<Context> mActivityContext;
    private static List<WSResponseListener> requestList = new ArrayList<>();
    private boolean isCancelled = false;
    private WeakReference<ResponseListener> mResponseListenerWeakReference;

    public WSResponseListener(Context context) {
        mActivityContext = new WeakReference<>(context);
        requestList.add(this);
    }

    @Override
    public void success(T t, Response response) {
        if (!isCancelled) {
            if (mResponseListenerWeakReference != null) {
                ResponseListener lResponseListener = mResponseListenerWeakReference.get();
                if (lResponseListener != null) {
                    lResponseListener.onResponse(t);
                }
            } else {
                onSuccess(t);
            }
        }
        requestList.remove(this);
    }

    @Override
    public void failure(RetrofitError error) {
        requestList.remove(this);
        if (isCancelled)
            return;
        if (error.isNetworkError()) {
            if (error.getCause() instanceof SocketTimeoutException) {
                _onError(new ErrorObject("Something went wrong!! Please try again later."));
                return;
            }
            if (error.getCause() instanceof UnknownHostException) {
                _onError(new ErrorObject("Please check internet connection"));
                return;
            }
        }

        ApiErrorResponse errorResponse;
        try {
            errorResponse = (ApiErrorResponse) error.getBodyAs(ApiErrorResponse.class);
        } catch (Exception e) {
            _onError(new ErrorObject("Something went wrong!! Please try again later."));
            return;
        }

        ErrorObject errorObject = null;
        if (errorResponse != null) {
            errorObject = errorResponse.error;
        } else {
            errorObject = new ErrorObject("Something went wrong!! Please try again later.");
        }
        _onError(errorObject);
        return;
    }


    void _onError(ErrorObject error) {

        if (mResponseListenerWeakReference != null) {
            ResponseListener lResponseListener = mResponseListenerWeakReference.get();
            if (lResponseListener != null) {
                lResponseListener.onError(error);
            }
        } else {
            onError(error);
        }
    }

    public void onSuccess(T result) {
    }

    public void onError(ErrorObject error) {
    }

}

