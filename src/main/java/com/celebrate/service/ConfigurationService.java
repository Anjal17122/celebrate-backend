package com.celebrate.service;

import com.celebrate.dto.input.*;
import com.celebrate.dto.response.ConfigurationResponse;
import com.celebrate.dto.response.DemoCredentialsResponse;
import com.celebrate.entity.ConfigurationEntity;
import com.celebrate.entity.VersionsEntity;
import com.celebrate.exception.*;
import com.celebrate.repository.ConfigurationRepository;
import com.celebrate.repository.VersionsRepository;
import com.celebrate.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;
    private final VersionsRepository versionsRepository;

    public ConfigurationResponse getConfiguration() {
        ConfigurationEntity config = getOrCreate();
        return mapToResponse(config);
    }

    @Transactional
    public ConfigurationResponse saveEmailConfiguration(EmailConfigurationInput input) {
        SecurityUtil.requireRole("ADMIN");
        ConfigurationEntity config = getOrCreate();
        config.setEmail(input.getEmail());
        config.setPassword(input.getPassword());
        config.setEmailName(input.getEmailName());
        config.setEnableEmail(input.getEnableEmail());
        return mapToResponse(configurationRepository.save(config));
    }

    @Transactional
    public ConfigurationResponse saveFormEmailConfiguration(FormEmailConfigurationInput input) {
        SecurityUtil.requireRole("ADMIN");
        ConfigurationEntity config = getOrCreate();
        config.setFormEmail(input.getFormEmail());
        return mapToResponse(configurationRepository.save(config));
    }

    @Transactional
    public ConfigurationResponse saveSendGridConfiguration(SendGridConfigurationInput input) {
        SecurityUtil.requireRole("ADMIN");
        ConfigurationEntity config = getOrCreate();
        config.setSendGridApiKey(input.getSendGridApiKey());
        config.setSendGridEnabled(input.getSendGridEnabled());
        config.setSendGridEmail(input.getSendGridEmail());
        config.setSendGridEmailName(input.getSendGridEmailName());
        config.setSendGridPassword(input.getSendGridPassword());
        return mapToResponse(configurationRepository.save(config));
    }

    @Transactional
    public ConfigurationResponse saveFirebaseConfiguration(FirebaseConfigurationInput input) {
        SecurityUtil.requireRole("ADMIN");
        ConfigurationEntity config = getOrCreate();
        config.setFirebaseKey(input.getFirebaseKey());
        config.setAuthDomain(input.getAuthDomain());
        config.setProjectId(input.getProjectId());
        config.setStorageBucket(input.getStorageBucket());
        config.setMsgSenderId(input.getMsgSenderId());
        config.setAppId(input.getAppId());
        config.setMeasurementId(input.getMeasurementId());
        config.setVapidKey(input.getVapidKey());
        return mapToResponse(configurationRepository.save(config));
    }

    @Transactional
    public ConfigurationResponse saveSentryConfiguration(SentryConfigurationInput input) {
        SecurityUtil.requireRole("ADMIN");
        ConfigurationEntity config = getOrCreate();
        config.setDashboardSentryUrl(input.getDashboardSentryUrl());
        config.setWebSentryUrl(input.getWebSentryUrl());
        config.setApiSentryUrl(input.getApiSentryUrl());
        config.setCustomerAppSentryUrl(input.getCustomerAppSentryUrl());
        config.setRestaurantAppSentryUrl(input.getRestaurantAppSentryUrl());
        config.setRiderAppSentryUrl(input.getRiderAppSentryUrl());
        return mapToResponse(configurationRepository.save(config));
    }

    @Transactional
    public ConfigurationResponse saveGoogleApiKeyConfiguration(GoogleApiKeyConfigurationInput input) {
        SecurityUtil.requireRole("ADMIN");
        ConfigurationEntity config = getOrCreate();
        config.setGoogleApiKey(input.getGoogleApiKey());
        return mapToResponse(configurationRepository.save(config));
    }

    @Transactional
    public ConfigurationResponse saveCloudinaryConfiguration(CloudinaryConfigurationInput input) {
        SecurityUtil.requireRole("ADMIN");
        ConfigurationEntity config = getOrCreate();
        config.setCloudinaryUploadUrl(input.getCloudinaryUploadUrl());
        config.setCloudinaryApiKey(input.getCloudinaryApiKey());
        return mapToResponse(configurationRepository.save(config));
    }

    @Transactional
    public ConfigurationResponse saveAmplitudeApiKeyConfiguration(AmplitudeApiKeyConfigurationInput input) {
        SecurityUtil.requireRole("ADMIN");
        ConfigurationEntity config = getOrCreate();
        config.setWebAmplitudeApiKey(input.getWebAmplitudeApiKey());
        config.setAppAmplitudeApiKey(input.getAppAmplitudeApiKey());
        return mapToResponse(configurationRepository.save(config));
    }

    @Transactional
    public ConfigurationResponse saveGoogleClientIDConfiguration(GoogleClientIDConfigurationInput input) {
        SecurityUtil.requireRole("ADMIN");
        ConfigurationEntity config = getOrCreate();
        config.setWebClientID(input.getWebClientID());
        config.setAndroidClientID(input.getAndroidClientID());
        config.setIOSClientID(input.getIOSClientID());
        config.setExpoClientID(input.getExpoClientID());
        return mapToResponse(configurationRepository.save(config));
    }

    @Transactional
    public ConfigurationResponse saveWebConfiguration(WebConfigurationInput input) {
        SecurityUtil.requireRole("ADMIN");
        ConfigurationEntity config = getOrCreate();
        config.setGoogleMapLibraries(input.getGoogleMapLibraries());
        config.setGoogleColor(input.getGoogleColor());
        return mapToResponse(configurationRepository.save(config));
    }

    @Transactional
    public ConfigurationResponse saveAppConfigurations(AppConfigurationsInput input) {
        SecurityUtil.requireRole("ADMIN");
        ConfigurationEntity config = getOrCreate();
        config.setTermsAndConditions(input.getTermsAndConditions());
        config.setPrivacyPolicy(input.getPrivacyPolicy());
        config.setTestOtp(input.getTestOtp());
        return mapToResponse(configurationRepository.save(config));
    }

    @Transactional
    public ConfigurationResponse saveDeliveryRateConfiguration(DeliveryCostConfigurationInput input) {
        SecurityUtil.requireRole("ADMIN");
        ConfigurationEntity config = getOrCreate();
        if (input != null) {
            if (input.getDeliveryRate() != null) config.setDeliveryRate(input.getDeliveryRate().doubleValue());
            if (input.getCostType() != null) config.setCostType(input.getCostType());
        }
        return mapToResponse(configurationRepository.save(config));
    }

    @Transactional
    public ConfigurationResponse savePaypalConfiguration(PaypalConfigurationInput input) {
        SecurityUtil.requireRole("ADMIN");
        ConfigurationEntity config = getOrCreate();
        config.setClientId(input.getClientId());
        config.setClientSecret(input.getClientSecret());
        config.setSandbox(input.getSandbox());
        return mapToResponse(configurationRepository.save(config));
    }

    @Transactional
    public ConfigurationResponse saveStripeConfiguration(StripeConfigurationInput input) {
        SecurityUtil.requireRole("ADMIN");
        ConfigurationEntity config = getOrCreate();
        config.setPublishableKey(input.getPublishableKey());
        config.setSecretKey(input.getSecretKey());
        return mapToResponse(configurationRepository.save(config));
    }

    @Transactional
    public ConfigurationResponse saveTwilioConfiguration(TwilioConfigurationInput input) {
        SecurityUtil.requireRole("ADMIN");
        ConfigurationEntity config = getOrCreate();
        config.setTwilioAccountSid(input.getTwilioAccountSid());
        config.setTwilioAuthToken(input.getTwilioAuthToken());
        config.setTwilioPhoneNumber(input.getTwilioPhoneNumber());
        config.setTwilioEnabled(input.getTwilioEnabled());
        config.setTwilioWhatsAppNumber(input.getTwilioWhatsAppNumber());
        return mapToResponse(configurationRepository.save(config));
    }

    @Transactional
    public ConfigurationResponse saveCurrencyConfiguration(CurrencyConfigurationInput input) {
        SecurityUtil.requireRole("ADMIN");
        ConfigurationEntity config = getOrCreate();
        config.setCurrency(input.getCurrency());
        config.setCurrencySymbol(input.getCurrencySymbol());
        return mapToResponse(configurationRepository.save(config));
    }

    @Transactional
    public ConfigurationResponse saveVerificationsToggle(VerificationConfigurationInput input) {
        SecurityUtil.requireRole("ADMIN");
        ConfigurationEntity config = getOrCreate();
        config.setSkipEmailVerification(input.getSkipEmailVerification());
        config.setSkipMobileVerification(input.getSkipMobileVerification());
        config.setSkipWhatsAppOTP(input.getSkipWhatsAppOTP());
        return mapToResponse(configurationRepository.save(config));
    }

    @Transactional
    public ConfigurationResponse saveDemoConfiguration(DemoConfigurationInput input) {
        SecurityUtil.requireRole("ADMIN");
        ConfigurationEntity config = getOrCreate();
        config.setEnableRiderDemo(input.getEnableRiderDemo());
        config.setEnableRestaurantDemo(input.getEnableRestaurantDemo());
        config.setEnableAdminDemo(input.getEnableAdminDemo());
        return mapToResponse(configurationRepository.save(config));
    }

    @Transactional
    public boolean setVersions(AppTypeInput customerAppVersion, AppTypeInput riderAppVersion, AppTypeInput restaurantAppVersion) {
        SecurityUtil.requireRole("ADMIN");
        VersionsEntity versions = versionsRepository.findFirstBy()
                .orElse(VersionsEntity.builder().build());

        if (customerAppVersion != null) {
            versions.setCustomerAppAndroid(customerAppVersion.getAndroid());
            versions.setCustomerAppIos(customerAppVersion.getIos());
        }
        if (riderAppVersion != null) {
            versions.setRiderAppAndroid(riderAppVersion.getAndroid());
            versions.setRiderAppIos(riderAppVersion.getIos());
        }
        if (restaurantAppVersion != null) {
            versions.setRestaurantAppAndroid(restaurantAppVersion.getAndroid());
            versions.setRestaurantAppIos(restaurantAppVersion.getIos());
        }

        versionsRepository.save(versions);
        return true;
    }

    public Object getVersions() {
        return versionsRepository.findFirstBy().orElse(null);
    }

    private ConfigurationEntity getOrCreate() {
        return configurationRepository.findFirstBy()
                .orElse(ConfigurationEntity.builder().build());
    }

    private ConfigurationResponse mapToResponse(ConfigurationEntity c) {
        return ConfigurationResponse.builder()
                .id(c.getId())
                .pushToken(c.getPushToken())
                .email(c.getEmail())
                .emailName(c.getEmailName())
                .password(c.getPassword())
                .enableEmail(c.getEnableEmail())
                .clientId(c.getClientId())
                .clientSecret(c.getClientSecret())
                .sandbox(c.getSandbox())
                .publishableKey(c.getPublishableKey())
                .secretKey(c.getSecretKey())
                .currency(c.getCurrency())
                .currencySymbol(c.getCurrencySymbol())
                .deliveryRate(c.getDeliveryRate())
                .twilioAccountSid(c.getTwilioAccountSid())
                .twilioAuthToken(c.getTwilioAuthToken())
                .twilioPhoneNumber(c.getTwilioPhoneNumber())
                .twilioEnabled(c.getTwilioEnabled())
                .twilioWhatsAppNumber(c.getTwilioWhatsAppNumber())
                .formEmail(c.getFormEmail())
                .sendGridApiKey(c.getSendGridApiKey())
                .sendGridEnabled(c.getSendGridEnabled())
                .sendGridEmail(c.getSendGridEmail())
                .sendGridEmailName(c.getSendGridEmailName())
                .sendGridPassword(c.getSendGridPassword())
                .dashboardSentryUrl(c.getDashboardSentryUrl())
                .webSentryUrl(c.getWebSentryUrl())
                .apiSentryUrl(c.getApiSentryUrl())
                .customerAppSentryUrl(c.getCustomerAppSentryUrl())
                .restaurantAppSentryUrl(c.getRestaurantAppSentryUrl())
                .riderAppSentryUrl(c.getRiderAppSentryUrl())
                .googleApiKey(c.getGoogleApiKey())
                .cloudinaryUploadUrl(c.getCloudinaryUploadUrl())
                .cloudinaryApiKey(c.getCloudinaryApiKey())
                .webAmplitudeApiKey(c.getWebAmplitudeApiKey())
                .appAmplitudeApiKey(c.getAppAmplitudeApiKey())
                .webClientID(c.getWebClientID())
                .androidClientID(c.getAndroidClientID())
                .iOSClientID(c.getIOSClientID())
                .expoClientID(c.getExpoClientID())
                .googleMapLibraries(c.getGoogleMapLibraries())
                .googleColor(c.getGoogleColor())
                .termsAndConditions(c.getTermsAndConditions())
                .privacyPolicy(c.getPrivacyPolicy())
                .testOtp(c.getTestOtp())
                .firebaseKey(c.getFirebaseKey())
                .authDomain(c.getAuthDomain())
                .projectId(c.getProjectId())
                .storageBucket(c.getStorageBucket())
                .msgSenderId(c.getMsgSenderId())
                .appId(c.getAppId())
                .measurementId(c.getMeasurementId())
                .isPaidVersion(c.getIsPaidVersion())
                .skipMobileVerification(c.getSkipMobileVerification())
                .skipEmailVerification(c.getSkipEmailVerification())
                .skipWhatsAppOTP(c.getSkipWhatsAppOTP())
                .enableRiderDemo(c.getEnableRiderDemo())
                .enableRestaurantDemo(c.getEnableRestaurantDemo())
                .enableAdminDemo(c.getEnableAdminDemo())
                .costType(c.getCostType())
                .vapidKey(c.getVapidKey())
                .build();
    }

    public DemoCredentialsResponse getLastOrderCreds() {
        return DemoCredentialsResponse.builder()
                .restaurantUsername(null)
                .restaurantPassword(null)
                .riderUsername(null)
                .riderPassword(null)
                .build();
    }
}
