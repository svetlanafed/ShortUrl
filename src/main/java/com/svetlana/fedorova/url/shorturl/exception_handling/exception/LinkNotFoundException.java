package com.svetlana.fedorova.url.shorturl.exception_handling.exception;

public class LinkNotFoundException extends RuntimeException {

    public LinkNotFoundException(String message) {
        super(message);
    }
}
