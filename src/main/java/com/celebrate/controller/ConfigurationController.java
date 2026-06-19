package com.celebrate.controller;

import com.celebrate.dto.input.*;
import com.celebrate.dto.response.ConfigurationResponse;
import com.celebrate.dto.response.DemoCredentialsResponse;
import com.celebrate.service.ConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ConfigurationController {

    private final ConfigurationService configurationService;

    @QueryMapping
    public ConfigurationResponse configuration() {
        return configurationService.getConfiguration();
    }

    @QueryMapping("getVersions")
    public Object getVersions() {
        return configurationService.getVersions();
    }

    @MutationMapping
    public ConfigurationResponse saveEmailConfiguration(@Argument EmailConfigurationInput configurationInput) {
        return configurationService.saveEmailConfiguration(configurationInput);
    }

    @MutationMapping
    public ConfigurationResponse saveTwilioConfiguration(@Argument TwilioConfigurationInput configurationInput) {
        return configurationService.saveTwilioConfiguration(configurationInput);
    }

    @MutationMapping
    public ConfigurationResponse saveSendGridConfiguration(@Argument SendGridConfigurationInput configurationInput) {
        return configurationService.saveSendGridConfiguration(configurationInput);
    }

    @MutationMapping
    public ConfigurationResponse saveFirebaseConfiguration(@Argument FirebaseConfigurationInput configurationInput) {
        return configurationService.saveFirebaseConfiguration(configurationInput);
    }

    @MutationMapping
    public ConfigurationResponse saveSentryConfiguration(@Argument SentryConfigurationInput configurationInput) {
        return configurationService.saveSentryConfiguration(configurationInput);
    }

    @MutationMapping
    public ConfigurationResponse saveGoogleApiKeyConfiguration(@Argument GoogleApiKeyConfigurationInput configurationInput) {
        return configurationService.saveGoogleApiKeyConfiguration(configurationInput);
    }

    @MutationMapping
    public ConfigurationResponse saveCloudinaryConfiguration(@Argument CloudinaryConfigurationInput configurationInput) {
        return configurationService.saveCloudinaryConfiguration(configurationInput);
    }

    @MutationMapping
    public ConfigurationResponse saveAmplitudeApiKeyConfiguration(@Argument AmplitudeApiKeyConfigurationInput configurationInput) {
        return configurationService.saveAmplitudeApiKeyConfiguration(configurationInput);
    }

    @MutationMapping
    public ConfigurationResponse saveGoogleClientIDConfiguration(@Argument GoogleClientIDConfigurationInput configurationInput) {
        return configurationService.saveGoogleClientIDConfiguration(configurationInput);
    }

    @MutationMapping
    public ConfigurationResponse saveWebConfiguration(@Argument WebConfigurationInput configurationInput) {
        return configurationService.saveWebConfiguration(configurationInput);
    }

    @MutationMapping
    public ConfigurationResponse saveAppConfigurations(@Argument AppConfigurationsInput configurationInput) {
        return configurationService.saveAppConfigurations(configurationInput);
    }

    @MutationMapping
    public ConfigurationResponse savePaypalConfiguration(@Argument PaypalConfigurationInput configurationInput) {
        return configurationService.savePaypalConfiguration(configurationInput);
    }

    @MutationMapping
    public ConfigurationResponse saveStripeConfiguration(@Argument StripeConfigurationInput configurationInput) {
        return configurationService.saveStripeConfiguration(configurationInput);
    }

    @MutationMapping
    public ConfigurationResponse saveCurrencyConfiguration(@Argument CurrencyConfigurationInput configurationInput) {
        return configurationService.saveCurrencyConfiguration(configurationInput);
    }

    @MutationMapping
    public ConfigurationResponse saveVerificationsToggle(@Argument VerificationConfigurationInput configurationInput) {
        return configurationService.saveVerificationsToggle(configurationInput);
    }

    @MutationMapping
    public ConfigurationResponse saveDemoConfiguration(@Argument DemoConfigurationInput configurationInput) {
        return configurationService.saveDemoConfiguration(configurationInput);
    }

    @MutationMapping
    public ConfigurationResponse saveDeliveryRateConfiguration(@Argument DeliveryCostConfigurationInput configurationInput) {
        return configurationService.saveDeliveryRateConfiguration(configurationInput);
    }

    @MutationMapping
    public ConfigurationResponse saveFormEmailConfiguration(@Argument FormEmailConfigurationInput configurationInput) {
        return configurationService.saveFormEmailConfiguration(configurationInput);
    }

    @MutationMapping
    public boolean setVersions(
            @Argument AppTypeInput customerAppVersion,
            @Argument AppTypeInput riderAppVersion,
            @Argument AppTypeInput restaurantAppVersion) {
        return configurationService.setVersions(customerAppVersion, riderAppVersion, restaurantAppVersion);
    }

    @QueryMapping
    public DemoCredentialsResponse lastOrderCreds() {
        return configurationService.getLastOrderCreds();
    }
}
