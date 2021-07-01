package com.backbase.exception;

public class OmdbApiServiceCallException extends CircuitBreakerException {
    public OmdbApiServiceCallException() {
    }

    public OmdbApiServiceCallException(String message) {
        super(message);
    }

    public OmdbApiServiceCallException(String message, Throwable cause) {
        super(message, cause);
    }

    public OmdbApiServiceCallException(Throwable cause) {
        super(cause);
    }

    public OmdbApiServiceCallException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
