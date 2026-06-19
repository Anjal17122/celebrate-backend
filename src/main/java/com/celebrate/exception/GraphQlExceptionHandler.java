package com.celebrate.exception;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

@Component
public class GraphQlExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        ErrorClassification errorType = toErrorType(ex);
        return GraphqlErrorBuilder.newError()
                .message(ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName())
                .errorType(errorType)
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
    }

    private ErrorClassification toErrorType(Throwable ex) {
        if (ex instanceof NotFoundException)       return CustomErrorType.NOT_FOUND;
        if (ex instanceof UnauthorizedException)   return CustomErrorType.UNAUTHORIZED;
        if (ex instanceof ForbiddenException)      return CustomErrorType.FORBIDDEN;
        if (ex instanceof BadRequestException)     return CustomErrorType.BAD_REQUEST;
        if (ex instanceof ConflictException)       return CustomErrorType.BAD_REQUEST;
        return CustomErrorType.INTERNAL_ERROR;
    }
}
