package com.payconiq.stock.exception;

public class StockDataIntegrityException extends BadRequestException {
    public StockDataIntegrityException() {
    }

    public StockDataIntegrityException(String message) {
        super(message);
    }

    public StockDataIntegrityException(String message, Throwable cause) {
        super(message, cause);
    }

    public StockDataIntegrityException(Throwable cause) {
        super(cause);
    }

    public StockDataIntegrityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
