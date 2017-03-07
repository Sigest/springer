package com.springer.exception;

public class IncorrectDocumentTypeException extends RuntimeException {
    public IncorrectDocumentTypeException() {
        super("Document class possible was extended but there is no ability to process this class still");
    }

    public IncorrectDocumentTypeException(String message) {
        super(message);
    }
}
