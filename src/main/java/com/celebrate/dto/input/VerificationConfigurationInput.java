package com.celebrate.dto.input;

import lombok.Data;

@Data
public class VerificationConfigurationInput {
    private Boolean skipEmailVerification;
    private Boolean skipMobileVerification;
    private Boolean skipWhatsAppOTP;
}
