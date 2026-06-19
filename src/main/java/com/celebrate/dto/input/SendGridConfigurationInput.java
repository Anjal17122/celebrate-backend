package com.celebrate.dto.input;

import lombok.Data;

@Data
public class SendGridConfigurationInput {
    private String sendGridApiKey;
    private Boolean sendGridEnabled;
    private String sendGridEmail;
    private String sendGridEmailName;
    private String sendGridPassword;
}
