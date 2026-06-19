package com.celebrate.controller;

import com.celebrate.dto.input.CouponInput;
import com.celebrate.dto.response.CouponResponse;
import com.celebrate.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @QueryMapping
    public List<CouponResponse> coupons() {
        return couponService.getAllCoupons();
    }

    @QueryMapping("restaurantCoupons")
    public List<CouponResponse> restaurantCoupons(@Argument String restaurantId) {
        return couponService.getRestaurantCoupons(restaurantId);
    }

    @MutationMapping
    public CouponResponse createCoupon(@Argument CouponInput couponInput) {
        return couponService.createCoupon(couponInput);
    }

    @MutationMapping
    public CouponResponse editCoupon(@Argument CouponInput couponInput) {
        return couponService.editCoupon(couponInput);
    }

    @MutationMapping
    public String deleteCoupon(@Argument String id) {
        return couponService.deleteCoupon(id);
    }

    @MutationMapping
    public CouponResponse createRestaurantCoupon(@Argument String restaurantId, @Argument CouponInput couponInput) {
        return couponService.createRestaurantCoupon(restaurantId, couponInput);
    }

    @MutationMapping
    public CouponResponse editRestaurantCoupon(@Argument String restaurantId, @Argument CouponInput couponInput) {
        return couponService.editRestaurantCoupon(restaurantId, couponInput);
    }

    @MutationMapping
    public String deleteRestaurantCoupon(@Argument String restaurantId, @Argument String couponId) {
        return couponService.deleteRestaurantCoupon(restaurantId, couponId);
    }

    // Schema: coupon(coupon: String!, restaurantId: ID!): CouponResponse! in Mutation block
    @MutationMapping
    public Map<String, Object> coupon(@Argument String coupon, @Argument String restaurantId) {
        return couponService.validateCoupon(coupon, restaurantId);
    }
}
