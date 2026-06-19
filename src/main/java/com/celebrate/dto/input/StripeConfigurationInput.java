package com.celebrate.dto.input;

import lombok.Data;

@Data
public class StripeConfigurationInput {
    private String publishableKey;
    private String secretKey;
}
