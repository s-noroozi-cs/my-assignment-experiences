package com.payconiq.stock.exception;

public class StockNotFoundException extends NotFoundException {
    public StockNotFoundException() {
    }

    public StockNotFoundException(String message) {
        super(message);
    }

    public StockNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public StockNotFoundException(Throwable cause) {
        super(cause);
    }

    public StockNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
