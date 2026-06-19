package com.celebrate.service;

import com.celebrate.dto.input.CouponInput;
import com.celebrate.dto.response.CouponResponse;
import com.celebrate.entity.CouponEntity;
import com.celebrate.entity.RestaurantEntity;
import com.celebrate.exception.*;
import com.celebrate.mapper.CouponMapper;
import com.celebrate.repository.CouponRepository;
import com.celebrate.repository.RestaurantRepository;
import com.celebrate.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final RestaurantRepository restaurantRepository;
    private final CouponMapper couponMapper;

    public List<CouponResponse> getAllCoupons() {
        SecurityUtil.requireRole("ADMIN");
        return couponRepository.findByRestaurantIsNull().stream().map(couponMapper::toResponse).toList();
    }

    public List<CouponResponse> getRestaurantCoupons(String restaurantId) {
        return couponRepository.findByRestaurantId(restaurantId).stream().map(couponMapper::toResponse).toList();
    }

    @Transactional
    public CouponResponse createCoupon(CouponInput input) {
        SecurityUtil.requireRole("ADMIN");
        CouponEntity coupon = buildCoupon(input, null);
        return couponMapper.toResponse(couponRepository.save(coupon));
    }

    @Transactional
    public CouponResponse editCoupon(CouponInput input) {
        SecurityUtil.requireRole("ADMIN");
        CouponEntity coupon = couponRepository.findById(input.getId())
                .orElseThrow(() -> new NotFoundException("Coupon", input.getId()));
        applyCouponUpdates(coupon, input);
        return couponMapper.toResponse(couponRepository.save(coupon));
    }

    @Transactional
    public String deleteCoupon(String id) {
        SecurityUtil.requireRole("ADMIN");
        CouponEntity coupon = couponRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Coupon", id));
        couponRepository.delete(coupon);
        return "Coupon deleted successfully.";
    }

    @Transactional
    public CouponResponse createRestaurantCoupon(String restaurantId, CouponInput input) {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant", restaurantId));
        CouponEntity coupon = buildCoupon(input, restaurant);
        return couponMapper.toResponse(couponRepository.save(coupon));
    }

    @Transactional
    public CouponResponse editRestaurantCoupon(String restaurantId, CouponInput input) {
        CouponEntity coupon = couponRepository.findById(input.getId())
                .orElseThrow(() -> new NotFoundException("Coupon", input.getId()));
        applyCouponUpdates(coupon, input);
        return couponMapper.toResponse(couponRepository.save(coupon));
    }

    @Transactional
    public String deleteRestaurantCoupon(String restaurantId, String couponId) {
        CouponEntity coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new NotFoundException("Coupon", couponId));
        couponRepository.delete(coupon);
        return "Coupon deleted successfully.";
    }

    public Map<String, Object> validateCoupon(String couponCode, String restaurantId) {
        CouponEntity coupon = couponRepository.findByTitleAndRestaurantIsNull(couponCode)
                .orElseGet(() -> couponRepository.findByTitleAndRestaurantId(couponCode, restaurantId).orElse(null));

        if (coupon == null || !Boolean.TRUE.equals(coupon.getEnabled())) {
            return Map.of("success", false, "message", "Invalid or expired coupon.", "coupon", null);
        }

        return Map.of(
                "success", true,
                "message", "Coupon applied successfully.",
                "coupon", Map.of(
                        "id", coupon.getId(),
                        "title", coupon.getTitle(),
                        "discount", coupon.getDiscount(),
                        "enabled", coupon.getEnabled()
                )
        );
    }

    private CouponEntity buildCoupon(CouponInput input, RestaurantEntity restaurant) {
        return CouponEntity.builder()
                .title(input.getTitle())
                .discount(input.getDiscount().doubleValue())
                .enabled(input.getEnabled() != null ? input.getEnabled() : true)
                .startDate(input.getStartDate())
                .endDate(input.getEndDate())
                .lifeTimeActive(input.getLifeTimeActive() != null ? input.getLifeTimeActive() : false)
                .restaurant(restaurant)
                .build();
    }

    private void applyCouponUpdates(CouponEntity coupon, CouponInput input) {
        if (input.getTitle() != null) coupon.setTitle(input.getTitle());
        if (input.getDiscount() != null) coupon.setDiscount(input.getDiscount().doubleValue());
        if (input.getEnabled() != null) coupon.setEnabled(input.getEnabled());
        if (input.getStartDate() != null) coupon.setStartDate(input.getStartDate());
        if (input.getEndDate() != null) coupon.setEndDate(input.getEndDate());
        if (input.getLifeTimeActive() != null) coupon.setLifeTimeActive(input.getLifeTimeActive());
    }
}
