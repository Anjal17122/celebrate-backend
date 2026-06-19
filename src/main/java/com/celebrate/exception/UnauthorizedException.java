package com.celebrate.exception;

public class UnauthorizedException extends CelebrateException {
    public UnauthorizedException(String message) {
        super(message);
    }
    public UnauthorizedException() {
        super("Authentication required");
    }
}
