package com.zzxx.exam.service;

public class YonHuException extends Exception {
    public YonHuException() {
    }

    public YonHuException(String message) {
        super(message);
    }

    public YonHuException(String message, Throwable cause) {
        super(message, cause);
    }

    public YonHuException(Throwable cause) {
        super(cause);
    }

    public YonHuException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
