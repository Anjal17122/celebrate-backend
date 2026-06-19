package com.celebrate.dto.input;

import lombok.Data;

@Data
public class EmailConfigurationInput {
    private String email;
    private String password;
    private String emailName;
    private Boolean enableEmail;
}
