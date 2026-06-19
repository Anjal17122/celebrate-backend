package com.celebrate.dto.input;

import lombok.Data;

@Data
public class TwilioConfigurationInput {
    private String twilioAccountSid;
    private String twilioAuthToken;
    private String twilioPhoneNumber;
    private Boolean twilioEnabled;
    private String twilioWhatsAppNumber;
}
