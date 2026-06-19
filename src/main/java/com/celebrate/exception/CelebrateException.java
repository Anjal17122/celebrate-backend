package com.celebrate.exception;

public class CelebrateException extends RuntimeException {
    public CelebrateException(String message) {
        super(message);
    }
    public CelebrateException(String message, Throwable cause) {
        super(message, cause);
    }
}
