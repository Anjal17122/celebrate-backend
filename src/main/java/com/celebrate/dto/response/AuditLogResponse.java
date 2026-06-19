package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponse {
    private String id;
    private String timestamp;
    private OwnerSimpleResponse admin;
    private String action;
    private String targetType;
    private String targetId;
    private Object changes;

    public String get_id() { return id; }
}
