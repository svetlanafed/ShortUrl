package com.svetlana.fedorova.url.shorturl.exception_handling.exception;

public class LinkExpiredException extends RuntimeException {

    public LinkExpiredException(String message) {
        super(message);
    }
}
