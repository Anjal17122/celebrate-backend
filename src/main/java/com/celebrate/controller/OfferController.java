package com.celebrate.controller;

import com.celebrate.dto.input.OfferInput;
import com.celebrate.dto.response.OfferResponse;
import com.celebrate.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    @QueryMapping
    public List<OfferResponse> offers() {
        return offerService.getAllOffers();
    }

    @MutationMapping
    public OfferResponse createOffer(@Argument("offer") OfferInput offer) {
        return offerService.createOffer(offer);
    }

    @MutationMapping
    public OfferResponse editOffer(@Argument("offer") OfferInput offer) {
        return offerService.editOffer(offer);
    }

    @MutationMapping
    public boolean deleteOffer(@Argument String id) {
        return offerService.deleteOffer(id);
    }

    // schema: addRestaurantToOffer(id: String!, restaurant: String!)
    @MutationMapping
    public OfferResponse addRestaurantToOffer(@Argument String id, @Argument String restaurant) {
        return offerService.addRestaurantToOffer(id, restaurant);
    }
}
