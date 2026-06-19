package com.celebrate.controller;

import com.celebrate.dto.input.UserInput;
import com.celebrate.dto.input.VendorInput;
import com.celebrate.dto.response.*;
import com.celebrate.service.AuthService;
import com.celebrate.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final VendorService vendorService;

    @MutationMapping
    public AuthDataResponse login(
            @Argument String appleId,
            @Argument String email,
            @Argument String password,
            @Argument String type,
            @Argument String name,
            @Argument String notificationToken,
            @Argument Boolean isActive) {
        return authService.login(appleId, email, password, type, name, notificationToken, isActive);
    }

    @MutationMapping
    public AuthDataResponse loginPasswordless(
            @Argument String email,
            @Argument String otp,
            @Argument String notificationToken) {
        return authService.loginPasswordless(email, otp, notificationToken);
    }

    @MutationMapping
    public AuthDataResponse loginWithSocial(
            @Argument String type,
            @Argument String idToken,
            @Argument String notificationToken) {
        return authService.loginWithSocialToken(type, idToken, notificationToken);
    }

    @MutationMapping
    public AuthDataResponse createUser(@Argument UserInput userInput) {
        return authService.createUser(userInput);
    }

    @MutationMapping
    public OwnerAuthDataResponse ownerLogin(@Argument String email, @Argument String password) {
        return authService.ownerLogin(email, password);
    }

    @MutationMapping
    public OwnerAuthDataResponse adminLogin(@Argument String email, @Argument String password) {
        return authService.adminLogin(email, password);
    }

    @MutationMapping
    public AuthDataResponse riderLogin(
            @Argument String username,
            @Argument String password,
            @Argument String notificationToken,
            @Argument String timeZone) {
        return authService.riderLogin(username, password, notificationToken, timeZone);
    }

    @MutationMapping
    public RestaurantAuthResponse restaurantLogin(
            @Argument String username,
            @Argument String password,
            @Argument String notificationToken) {
        return authService.restaurantLogin(username, password, notificationToken);
    }

    @MutationMapping
    public OtpResultResponse sendOtpToEmail(@Argument String email) {
        return authService.sendOtpToEmail(email);
    }

    @MutationMapping
    public OtpResultResponse sendOtpToPhoneNumber(@Argument String phone) {
        return authService.sendOtpToPhone(phone);
    }

    @MutationMapping
    public VerifyOtpResultResponse verifyOtp(
            @Argument String otp,
            @Argument String email,
            @Argument String phone) {
        return authService.verifyOtp(otp, email, phone);
    }

    @MutationMapping
    public ForgotPasswordResponse forgotPassword(@Argument String email) {
        return authService.forgotPassword(email);
    }

    @MutationMapping
    public ForgotPasswordResponse resetPassword(@Argument String password, @Argument String email) {
        return authService.resetPassword(password, email);
    }

    @MutationMapping
    public boolean vendorResetPassword(@Argument String oldPassword, @Argument String newPassword) {
        return authService.vendorResetPassword(oldPassword, newPassword);
    }

    @MutationMapping
    public boolean changePassword(@Argument String oldPassword, @Argument String newPassword) {
        return authService.changePassword(oldPassword, newPassword);
    }
}
