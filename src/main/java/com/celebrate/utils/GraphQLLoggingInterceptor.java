package com.celebrate.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GraphQLLoggingInterceptor implements WebGraphQlInterceptor {

    @Override
    public Mono<WebGraphQlResponse> intercept(
            WebGraphQlRequest request,
            Chain chain) {

        log.info("GraphQL Query:\n{}", request.getDocument());
        log.info("Variables: {}", request.getVariables());

        return chain.next(request)
                .doOnNext(response -> {
                    log.info("GraphQL Response: {}", response.getExecutionResult().toSpecification());
                });
    }
}