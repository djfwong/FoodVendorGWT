package com.sneakyxpress.webapp.client.facebook;

public class NoUserInfoException extends Exception {
    public NoUserInfoException() {
    }

    public NoUserInfoException(String message) {
        super(message);
    }
}