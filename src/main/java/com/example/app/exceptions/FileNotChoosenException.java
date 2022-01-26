package com.example.app.exceptions;

public class FileNotChoosenException extends Exception {
    private final String message;
    public FileNotChoosenException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
