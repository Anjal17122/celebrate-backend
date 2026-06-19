package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerSimpleResponse {
    private String id;
    private String email;
    private Boolean isActive;

    public String get_id() { return id; }
}
