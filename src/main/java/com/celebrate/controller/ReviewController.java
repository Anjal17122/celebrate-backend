package com.celebrate.controller;

import com.celebrate.dto.response.ReviewResponse;
import com.celebrate.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @QueryMapping
    public List<ReviewResponse> reviews(@Argument Integer offset, @Argument("restaurant") String restaurantId) {
        return reviewService.getReviews(offset, restaurantId);
    }

    @QueryMapping
    public Map<String, Object> reviewsByRestaurant(@Argument("restaurant") String restaurantId) {
        return reviewService.getReviewsByRestaurant(restaurantId);
    }
}
