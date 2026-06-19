package com.celebrate.dto.input;

import lombok.Data;

@Data
public class UpdateUserInput {
    private String name;
    private String phone;
    private Boolean phoneIsVerified;
    private Boolean emailIsVerified;
}
