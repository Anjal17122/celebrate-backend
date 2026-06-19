package com.celebrate.controller;

import com.celebrate.dto.input.*;
import com.celebrate.dto.response.*;
import com.celebrate.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @QueryMapping
    public RestaurantResponse restaurant(@Argument String id, @Argument String slug) {
        return restaurantService.getRestaurant(id, slug);
    }

    @QueryMapping
    public List<RestaurantResponse> restaurants(@Argument Integer page, @Argument Integer limit) {
        return restaurantService.getRestaurants(page, limit);
    }

    @QueryMapping
    public List<RestaurantResponse> restaurantList() {
        return restaurantService.getRestaurantList();
    }

    @QueryMapping
    public Map<String, Object> restaurantsPaginated(@Argument Integer page, @Argument Integer limit, @Argument String search) {
        return restaurantService.getRestaurantsPaginated(page, limit, search);
    }

    @QueryMapping
    public Map<String, Object> getClonedRestaurantsPaginated(@Argument Integer page, @Argument Integer limit, @Argument String search) {
        return restaurantService.getClonedRestaurantsPaginated(page, limit, search);
    }

    @QueryMapping
    public List<RestaurantResponse> getClonedRestaurants() {
        return restaurantService.getClonedRestaurants();
    }

    @QueryMapping
    public Map<String, Object> commissionRate(@Argument Integer page, @Argument Integer limit) {
        return restaurantService.getCommissionRate(page, limit);
    }

    @QueryMapping
    public int pageCount(@Argument String restaurant) {
        return restaurantService.getPageCount(restaurant);
    }

    @QueryMapping
    public Map<String, Object> getRestaurantDeliveryZoneInfo(@Argument String id) {
        return restaurantService.getRestaurantDeliveryZoneInfo(id);
    }

    @MutationMapping
    public RestaurantResponse createRestaurant(@Argument RestaurantInput restaurant, @Argument String owner) {
        return restaurantService.createRestaurant(restaurant, owner);
    }

    @MutationMapping
    public RestaurantResponse editRestaurant(@Argument RestaurantProfileInput restaurant) {
        return restaurantService.editRestaurant(restaurant);
    }

    @MutationMapping
    public RestaurantResponse deleteRestaurant(@Argument String id) {
        return restaurantService.deleteRestaurant(id);
    }

    @MutationMapping
    public Boolean hardDeleteRestaurant(@Argument String id) {
        return restaurantService.hardDeleteRestaurant(id);
    }

    @MutationMapping
    public RestaurantResponse duplicateRestaurant(@Argument String id, @Argument String owner) {
        return restaurantService.duplicateRestaurant(id, owner);
    }

    @MutationMapping
    public RestaurantResponse toggleStoreAvailability(@Argument String restaurantId) {
        return restaurantService.toggleStoreAvailability(restaurantId);
    }

    @MutationMapping
    public RestaurantResponse toggleAvailability(@Argument String restaurantId) {
        return restaurantService.toggleAvailability(restaurantId);
    }

    @MutationMapping
    public RestaurantResponse updateTimings(@Argument String id, @Argument List<TimingsInput> openingTimes) {
        return restaurantService.updateTimings(id, openingTimes);
    }

    @MutationMapping
    public RestaurantResponse updateCommission(@Argument String id, @Argument Float commissionRate) {
        return restaurantService.updateCommission(id, commissionRate);
    }

    @MutationMapping
    public RestaurantOperationResponse updateDeliveryBoundsAndLocation(
            @Argument String id,
            @Argument String boundType,
            @Argument List<List<List<Float>>> bounds,
            @Argument CircleBoundsInput circleBounds,
            @Argument CoordinatesInput location,
            @Argument String address,
            @Argument String postCode,
            @Argument String city) {
        return restaurantService.updateDeliveryBoundsAndLocation(id, boundType, bounds, circleBounds, location, address, postCode, city);
    }

    @MutationMapping
    public RestaurantOperationResponse updateRestaurantDelivery(
            @Argument String id,
            @Argument Float minDeliveryFee,
            @Argument Float deliveryDistance,
            @Argument Float deliveryFee) {
        return restaurantService.updateRestaurantDelivery(id, minDeliveryFee, deliveryDistance, deliveryFee);
    }

    @MutationMapping
    public RestaurantOperationResponse updateRestaurantBussinessDetails(
            @Argument String id,
            @Argument BussinessDetailsInput bussinessDetails) {
        return restaurantService.updateRestaurantBussinessDetails(id, bussinessDetails);
    }

    @MutationMapping
    public RestaurantResponse saveRestaurantToken(@Argument String token, @Argument Boolean isEnabled) {
        return restaurantService.saveRestaurantToken(token, isEnabled);
    }

    @QueryMapping
    public RestaurantResponse restaurantPreview(@Argument String id, @Argument String slug) {
        return restaurantService.getRestaurantPreview(id, slug);
    }

    @QueryMapping
    public List<RestaurantResponse> restaurantListPreview() {
        return restaurantService.getRestaurantListPreview();
    }

    @QueryMapping
    public List<RestaurantResponse> restaurantsPreview() {
        return restaurantService.getRestaurantsPreview();
    }

    @QueryMapping
    public Map<String, Object> nearByRestaurants(@Argument Float latitude, @Argument Float longitude, @Argument String shopType) {
        return restaurantService.nearByRestaurants(latitude, longitude, shopType);
    }

    @QueryMapping
    public Map<String, Object> nearByRestaurantsPreview(@Argument Float latitude, @Argument Float longitude,
                                                         @Argument String shopType, @Argument Integer page, @Argument Integer limit) {
        return restaurantService.nearByRestaurantsPreview(latitude, longitude, shopType, page, limit);
    }

    @QueryMapping
    public List<RestaurantResponse> recentOrderRestaurants(@Argument Float latitude, @Argument Float longitude) {
        return restaurantService.recentOrderRestaurants(latitude, longitude);
    }

    @QueryMapping
    public List<RestaurantResponse> recentOrderRestaurantsPreview(@Argument Float latitude, @Argument Float longitude) {
        return restaurantService.recentOrderRestaurantsPreview(latitude, longitude);
    }

    @QueryMapping
    public List<RestaurantResponse> mostOrderedRestaurants(@Argument Float latitude, @Argument Float longitude) {
        return restaurantService.mostOrderedRestaurants(latitude, longitude);
    }

    @QueryMapping
    public List<RestaurantResponse> mostOrderedRestaurantsPreview(@Argument Float latitude, @Argument Float longitude,
                                                                   @Argument String shopType, @Argument Integer page, @Argument Integer limit) {
        return restaurantService.mostOrderedRestaurantsPreview(latitude, longitude, shopType, page, limit);
    }

    @QueryMapping
    public List<RestaurantResponse> topRatedVendors(@Argument Float latitude, @Argument Float longitude) {
        return restaurantService.topRatedVendors(latitude, longitude);
    }

    @QueryMapping
    public List<RestaurantResponse> topRatedVendorsPreview(@Argument Float latitude, @Argument Float longitude) {
        return restaurantService.topRatedVendorsPreview(latitude, longitude);
    }

    @QueryMapping
    public List<Map<String, Object>> getRestaurantMenuItems(@Argument String restaurantId) {
        return restaurantService.getRestaurantMenuItems(restaurantId);
    }

    @QueryMapping
    public List<String> relatedItems(@Argument String itemId, @Argument String restaurantId) {
        return restaurantService.getRelatedItems(itemId, restaurantId);
    }

    @QueryMapping
    public List<Map<String, Object>> fetchCategoryDetailsByStoreId(@Argument String storeId) {
        return restaurantService.fetchCategoryDetailsByStoreId(storeId);
    }

    @QueryMapping
    public List<Map<String, Object>> fetchCategoryDetailsByStoreIdForMobile(@Argument String storeId) {
        return restaurantService.fetchCategoryDetailsByStoreIdForMobile(storeId);
    }

    @QueryMapping
    public List<Map<String, Object>> popularItems(@Argument String restaurantId) {
        return restaurantService.getPopularItems(restaurantId);
    }

    @QueryMapping
    public List<FoodResponse> popularFoodItems(@Argument String restaurantId) {
        return restaurantService.getPopularFoodItems(restaurantId);
    }
}
