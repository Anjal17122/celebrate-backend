package com.celebrate.controller;

import com.celebrate.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @QueryMapping
    public Map<String, Object> auditLogs(@Argument Integer page, @Argument Integer limit) {
        return auditLogService.getAuditLogs(page, limit);
    }
}
