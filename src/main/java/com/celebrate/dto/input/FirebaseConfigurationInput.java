package com.celebrate.dto.input;

import lombok.Data;

@Data
public class FirebaseConfigurationInput {
    private String firebaseKey;
    private String authDomain;
    private String projectId;
    private String storageBucket;
    private String msgSenderId;
    private String appId;
    private String measurementId;
    private String vapidKey;
}
