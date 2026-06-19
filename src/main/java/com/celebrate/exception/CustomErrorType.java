package com.celebrate.exception;

import graphql.ErrorClassification;

public enum CustomErrorType implements ErrorClassification {
    NOT_FOUND,
    UNAUTHORIZED,
    FORBIDDEN,
    BAD_REQUEST,
    INTERNAL_ERROR
}
