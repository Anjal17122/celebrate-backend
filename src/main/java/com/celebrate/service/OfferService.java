package com.celebrate.service;

import com.celebrate.dto.input.OfferInput;
import com.celebrate.dto.response.OfferResponse;
import com.celebrate.entity.OfferEntity;
import com.celebrate.entity.RestaurantEntity;
import com.celebrate.exception.*;
import com.celebrate.mapper.OfferMapper;
import com.celebrate.repository.OfferRepository;
import com.celebrate.repository.RestaurantRepository;
import com.celebrate.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OfferMapper offerMapper;

    public List<OfferResponse> getAllOffers() {
        return offerRepository.findAll().stream().map(offerMapper::toResponse).toList();
    }

    @Transactional
    public OfferResponse createOffer(OfferInput input) {
        SecurityUtil.requireRole("ADMIN");
        List<RestaurantEntity> restaurants = new ArrayList<>();
        if (input.getRestaurants() != null) {
            for (String rId : input.getRestaurants()) {
                restaurantRepository.findById(rId).ifPresent(restaurants::add);
            }
        }
        OfferEntity offer = OfferEntity.builder()
                .name(input.getName())
                .tag(input.getTag())
                .restaurants(restaurants)
                .build();
        return offerMapper.toResponse(offerRepository.save(offer));
    }

    @Transactional
    public OfferResponse editOffer(OfferInput input) {
        SecurityUtil.requireRole("ADMIN");
        OfferEntity offer = offerRepository.findById(input.getId())
                .orElseThrow(() -> new NotFoundException("Offer", input.getId()));
        if (input.getName() != null) offer.setName(input.getName());
        if (input.getTag() != null) offer.setTag(input.getTag());
        if (input.getRestaurants() != null) {
            List<RestaurantEntity> restaurants = new ArrayList<>();
            for (String rId : input.getRestaurants()) {
                restaurantRepository.findById(rId).ifPresent(restaurants::add);
            }
            offer.setRestaurants(restaurants);
        }
        return offerMapper.toResponse(offerRepository.save(offer));
    }

    @Transactional
    public boolean deleteOffer(String id) {
        SecurityUtil.requireRole("ADMIN");
        OfferEntity offer = offerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Offer", id));
        offerRepository.delete(offer);
        return true;
    }

    @Transactional
    public OfferResponse addRestaurantToOffer(String offerId, String restaurantId) {
        SecurityUtil.requireRole("ADMIN");
        OfferEntity offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new NotFoundException("Offer", offerId));
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant", restaurantId));

        if (offer.getRestaurants() == null) offer.setRestaurants(new ArrayList<>());
        if (!offer.getRestaurants().contains(restaurant)) {
            offer.getRestaurants().add(restaurant);
        }
        return offerMapper.toResponse(offerRepository.save(offer));
    }
}
