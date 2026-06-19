package com.celebrate.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffResponse {
    private String id;
    private String name;
    private String email;
    private String password;
    private String plainPassword;
    private String phone;
    private Boolean isActive;
    private List<String> permissions;
    private String userType;

    public String get_id() { return id; }
}
