package com.celebrate.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationResponse {
    private String id;
    private String pushToken;
    private String email;
    private String emailName;
    private String password;
    private Boolean enableEmail;
    private String clientId;
    private String clientSecret;
    private Boolean sandbox;
    private String publishableKey;
    private String secretKey;
    private String currency;
    private String currencySymbol;
    private Double deliveryRate;
    private String twilioAccountSid;
    private String twilioAuthToken;
    private String twilioPhoneNumber;
    private String twilioWhatsAppNumber;
    private Boolean twilioEnabled;
    private String formEmail;
    private String sendGridApiKey;
    private Boolean sendGridEnabled;
    private String sendGridEmail;
    private String sendGridEmailName;
    private String sendGridPassword;
    private String dashboardSentryUrl;
    private String webSentryUrl;
    private String apiSentryUrl;
    private String customerAppSentryUrl;
    private String restaurantAppSentryUrl;
    private String riderAppSentryUrl;
    private String googleApiKey;
    private String cloudinaryUploadUrl;
    private String cloudinaryApiKey;
    private String webAmplitudeApiKey;
    private String appAmplitudeApiKey;
    private String webClientID;
    private String androidClientID;
    private String iOSClientID;
    private String expoClientID;
    private String googleMapLibraries;
    private String googleColor;
    private String termsAndConditions;
    private String privacyPolicy;
    private String testOtp;
    private String firebaseKey;
    private String authDomain;
    private String projectId;
    private String storageBucket;
    private String msgSenderId;
    private String appId;
    private String measurementId;
    private Boolean isPaidVersion;
    private Boolean skipMobileVerification;
    private Boolean skipEmailVerification;
    private Boolean skipWhatsAppOTP;
    private Boolean enableRiderDemo;
    private Boolean enableRestaurantDemo;
    private Boolean enableAdminDemo;
    private String costType;
    private String vapidKey;

    public String get_id() { return id; }
}
