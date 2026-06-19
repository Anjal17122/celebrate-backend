package com.celebrate.controller;

import com.celebrate.dto.input.AddressInput;
import com.celebrate.dto.input.UpdateUserInput;
import com.celebrate.dto.response.RestaurantResponse;
import com.celebrate.dto.response.SaveNotificationTokenWebResponse;
import com.celebrate.dto.response.UserResponse;
import com.celebrate.service.NotificationService;
import com.celebrate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final NotificationService notificationService;

    @QueryMapping
    public UserResponse profile() {
        return userService.getProfile();
    }

    @QueryMapping
    public List<UserResponse> users(@Argument Integer page, @Argument Integer limit) {
        return userService.getUsers(page, limit);
    }

    @QueryMapping
    public UserResponse user(@Argument String id) {
        return userService.getUserById(id);
    }

    @MutationMapping
    public UserResponse updateUser(@Argument UpdateUserInput updateUserInput) {
        return userService.updateUser(updateUserInput);
    }

    @MutationMapping
    public UserResponse updateNotificationStatus(
            @Argument Boolean offerNotification,
            @Argument Boolean orderNotification) {
        return userService.updateNotificationStatus(offerNotification, orderNotification);
    }

    @MutationMapping
    public UserResponse pushToken(@Argument String token) {
        return userService.pushToken(token);
    }

    @MutationMapping
    public UserResponse updateUserStatus(@Argument String id, @Argument String status, @Argument String reason) {
        return userService.updateUserStatus(id, status, reason);
    }

    @MutationMapping
    public UserResponse updateUserNotes(@Argument String id, @Argument String notes) {
        return userService.updateUserNotes(id, notes);
    }

    @MutationMapping
    public UserResponse deleteUser(@Argument String id) {
        return userService.deleteUser(id);
    }

    @MutationMapping
    public UserResponse Deactivate(@Argument Boolean isActive, @Argument String email) {
        return userService.deactivate(isActive, email);
    }

    @MutationMapping
    public UserResponse resetUserSession(@Argument String userId) {
        return userService.resetUserSession(userId);
    }

    @MutationMapping
    public UserResponse emailExist(@Argument String email) {
        return userService.emailExist(email);
    }

    @MutationMapping
    public UserResponse phoneExist(@Argument String phone) {
        return userService.phoneExist(phone);
    }

    // Address mutations
    @MutationMapping
    public UserResponse createAddress(@Argument AddressInput addressInput) {
        return userService.createAddress(addressInput);
    }

    @MutationMapping
    public UserResponse editAddress(@Argument AddressInput addressInput) {
        return userService.editAddress(addressInput);
    }

    @MutationMapping
    public UserResponse deleteAddress(@Argument String id) {
        return userService.deleteAddress(id);
    }

    @MutationMapping
    public UserResponse deleteBulkAddresses(@Argument List<String> ids) {
        return userService.deleteBulkAddresses(ids);
    }

    @MutationMapping
    public UserResponse selectAddress(@Argument String id) {
        return userService.selectAddress(id);
    }

    @MutationMapping
    public UserResponse addFavourite(@Argument String id) {
        return userService.addFavourite(id);
    }

    @MutationMapping
    public SaveNotificationTokenWebResponse saveNotificationTokenWeb(@Argument String token) {
        return userService.saveNotificationTokenWeb(token);
    }

    @QueryMapping
    public List<RestaurantResponse> userFavourite(@Argument Float latitude, @Argument Float longitude) {
        return userService.getUserFavourite(latitude, longitude);
    }
}
