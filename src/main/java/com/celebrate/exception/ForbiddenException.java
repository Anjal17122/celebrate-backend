package com.celebrate.exception;

public class ForbiddenException extends CelebrateException {
    public ForbiddenException(String message) {
        super(message);
    }
    public ForbiddenException() {
        super("You do not have permission to perform this action");
    }
}
