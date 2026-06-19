package com.celebrate.dto.input;

import lombok.Data;

@Data
public class GoogleClientIDConfigurationInput {
    private String webClientID;
    private String androidClientID;
    private String iOSClientID;
    private String expoClientID;
}
