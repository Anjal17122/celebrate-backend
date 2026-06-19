package com.celebrate.service;

import com.celebrate.dto.response.RestaurantResponse;
import com.celebrate.dto.response.ReviewResponse;
import com.celebrate.entity.ReviewEntity;
import com.celebrate.mapper.RestaurantMapper;
import com.celebrate.mapper.OrderMapper;
import com.celebrate.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantMapper restaurantMapper;
    private final OrderMapper orderMapper;

    public List<ReviewResponse> getReviews(Integer offset, String restaurantId) {
        int page = offset != null ? offset : 0;
        List<ReviewEntity> reviews = reviewRepository.findByRestaurantId(
                restaurantId, PageRequest.of(page, 10, Sort.by("createdAt").descending()));
        return reviews.stream().map(this::toResponse).toList();
    }

    public Map<String, Object> getReviewsByRestaurant(String restaurantId) {
        List<ReviewEntity> reviews = reviewRepository.findByRestaurantId(restaurantId);
        Double avg = reviewRepository.findAverageRatingByRestaurantId(restaurantId);
        double ratings = avg != null ? avg : 0.0;
        return Map.of(
                "reviews", reviews.stream().map(this::toResponse).toList(),
                "ratings", ratings,
                "total", reviews.size()
        );
    }

    private ReviewResponse toResponse(ReviewEntity e) {
        return ReviewResponse.builder()
                .id(e.getId())
                .order(e.getOrder() != null ? orderMapper.toResponse(e.getOrder()) : null)
                .restaurant(e.getRestaurant() != null ? restaurantMapper.toResponse(e.getRestaurant()) : null)
                .rating(e.getRating())
                .description(e.getDescription())
                .comments(e.getComments())
                .isActive(e.getIsActive())
                .createdAt(e.getCreatedAt() != null ? e.getCreatedAt().toString() : null)
                .updatedAt(e.getUpdatedAt() != null ? e.getUpdatedAt().toString() : null)
                .build();
    }
}
