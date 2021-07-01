package com.backbase.exception;

public class ServiceAccessDeniedException extends RuntimeException {
    public ServiceAccessDeniedException() {
    }

    public ServiceAccessDeniedException(String message) {
        super(message);
    }

    public ServiceAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceAccessDeniedException(Throwable cause) {
        super(cause);
    }

    public ServiceAccessDeniedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
