package com.sneakyxpress.webapp.client.facebook;

public class NotLoggedInException extends Exception {
    public NotLoggedInException() {
    }

    public NotLoggedInException(String message) {
        super(message);
    }
}