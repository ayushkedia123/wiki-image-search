package com.ayush.wikisearch.model;

/**
 * Created by ayushkedia on 03/06/16.
 */
public class ErrorObject {

    public String code;
    public String info;

    public ErrorObject(String info) {
        super();
        this.info = info;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        if (info != null) {
            return info;
        } else {
            return "Something went wrong!! Please try again later.";
        }
    }

    public void setErrorMessage(String errorMessage) {
        this.info = errorMessage;
    }

}
