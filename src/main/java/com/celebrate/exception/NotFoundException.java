package com.celebrate.exception;

public class NotFoundException extends CelebrateException {
    public NotFoundException(String resource, String id) {
        super(resource + " not found with id: " + id);
    }
    public NotFoundException(String message) {
        super(message);
    }
}
