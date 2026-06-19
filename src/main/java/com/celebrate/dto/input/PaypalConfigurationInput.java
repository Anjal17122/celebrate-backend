package com.celebrate.dto.input;

import lombok.Data;

@Data
public class PaypalConfigurationInput {
    private String clientId;
    private String clientSecret;
    private Boolean sandbox;
}
