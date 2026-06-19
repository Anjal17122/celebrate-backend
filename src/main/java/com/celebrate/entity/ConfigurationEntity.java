package com.celebrate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "configuration")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "push_token", columnDefinition = "TEXT")
    private String pushToken;

    private String email;

    @Column(name = "email_name")
    private String emailName;

    @Column(length = 255)
    private String password;

    @Column(name = "enable_email")
    private Boolean enableEmail;

    // PayPal
    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_secret")
    private String clientSecret;

    private Boolean sandbox;

    // Stripe
    @Column(name = "publishable_key")
    private String publishableKey;

    @Column(name = "secret_key")
    private String secretKey;

    // Currency
    private String currency;

    @Column(name = "currency_symbol", length = 10)
    private String currencySymbol;

    @Column(name = "delivery_rate")
    private Double deliveryRate;

    @Column(name = "cost_type", length = 50)
    private String costType;

    // Twilio
    @Column(name = "twilio_account_sid")
    private String twilioAccountSid;

    @Column(name = "twilio_auth_token")
    private String twilioAuthToken;

    @Column(name = "twilio_phone_number")
    private String twilioPhoneNumber;

    @Column(name = "twilio_whatsapp_number")
    private String twilioWhatsAppNumber;

    @Column(name = "twilio_enabled")
    private Boolean twilioEnabled;

    // SendGrid
    @Column(name = "form_email")
    private String formEmail;

    @Column(name = "sendgrid_api_key")
    private String sendGridApiKey;

    @Column(name = "sendgrid_enabled")
    private Boolean sendGridEnabled;

    @Column(name = "sendgrid_email")
    private String sendGridEmail;

    @Column(name = "sendgrid_email_name")
    private String sendGridEmailName;

    @Column(name = "sendgrid_password")
    private String sendGridPassword;

    // Sentry
    @Column(name = "dashboard_sentry_url", length = 500)
    private String dashboardSentryUrl;

    @Column(name = "web_sentry_url", length = 500)
    private String webSentryUrl;

    @Column(name = "api_sentry_url", length = 500)
    private String apiSentryUrl;

    @Column(name = "customer_app_sentry_url", length = 500)
    private String customerAppSentryUrl;

    @Column(name = "restaurant_app_sentry_url", length = 500)
    private String restaurantAppSentryUrl;

    @Column(name = "rider_app_sentry_url", length = 500)
    private String riderAppSentryUrl;

    // Google
    @Column(name = "google_api_key", length = 500)
    private String googleApiKey;

    // Cloudinary
    @Column(name = "cloudinary_upload_url", length = 500)
    private String cloudinaryUploadUrl;

    @Column(name = "cloudinary_api_key")
    private String cloudinaryApiKey;

    // Amplitude
    @Column(name = "web_amplitude_api_key")
    private String webAmplitudeApiKey;

    @Column(name = "app_amplitude_api_key")
    private String appAmplitudeApiKey;

    // Google OAuth
    @Column(name = "web_client_id")
    private String webClientID;

    @Column(name = "android_client_id")
    private String androidClientID;

    @Column(name = "ios_client_id")
    private String iOSClientID;

    @Column(name = "expo_client_id")
    private String expoClientID;

    @Column(name = "google_map_libraries")
    private String googleMapLibraries;

    @Column(name = "google_color")
    private String googleColor;

    // App configs
    @Column(name = "terms_and_conditions", columnDefinition = "TEXT")
    private String termsAndConditions;

    @Column(name = "privacy_policy", columnDefinition = "TEXT")
    private String privacyPolicy;

    @Column(name = "test_otp", length = 10)
    private String testOtp;

    // Firebase
    @Column(name = "firebase_key")
    private String firebaseKey;

    @Column(name = "auth_domain")
    private String authDomain;

    @Column(name = "project_id")
    private String projectId;

    @Column(name = "storage_bucket")
    private String storageBucket;

    @Column(name = "msg_sender_id")
    private String msgSenderId;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "measurement_id")
    private String measurementId;

    @Column(name = "vapid_key")
    private String vapidKey;

    // Flags
    @Column(name = "is_paid_version")
    private Boolean isPaidVersion;

    @Column(name = "skip_mobile_verification")
    private Boolean skipMobileVerification;

    @Column(name = "skip_email_verification")
    private Boolean skipEmailVerification;

    @Column(name = "skip_whatsapp_otp")
    private Boolean skipWhatsAppOTP;

    @Column(name = "enable_rider_demo")
    private Boolean enableRiderDemo;

    @Column(name = "enable_restaurant_demo")
    private Boolean enableRestaurantDemo;

    @Column(name = "enable_admin_demo")
    private Boolean enableAdminDemo;
}
