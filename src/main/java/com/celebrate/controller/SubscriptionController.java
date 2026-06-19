package com.celebrate.controller;

import com.celebrate.dto.response.*;
import com.celebrate.service.SubscriptionPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionPublisher subscriptionPublisher;

    @SubscriptionMapping
    public Flux<SubscriptionOrdersResponse> subscribePlaceOrder(@Argument String restaurant) {
        return subscriptionPublisher.getOrderFlux()
                .filter(s -> restaurant.equals(s.getRestaurantId()));
    }

    @SubscriptionMapping
    public Flux<SubscriptionOrdersResponse> orderStatusChanged(@Argument String userId) {
        return subscriptionPublisher.getOrderFlux()
                .filter(s -> userId.equals(s.getUserId()));
    }

    @SubscriptionMapping
    public Flux<SubscriptionOrdersResponse> subscriptionAssignRider(@Argument String riderId) {
        return subscriptionPublisher.getOrderFlux()
                .filter(s -> riderId.equals(s.getRiderId()));
    }

    @SubscriptionMapping
    public Flux<RiderResponse> subscriptionRiderLocation(@Argument String riderId) {
        return subscriptionPublisher.getRiderLocationFlux()
                .filter(r -> riderId.equals(r.getId()));
    }

    @SubscriptionMapping
    public Flux<SubscriptionZoneOrdersResponse> subscriptionZoneOrders(@Argument String zoneId) {
        return subscriptionPublisher.getZoneOrderFlux()
                .filter(s -> zoneId.equals(s.getZoneId()));
    }

    @SubscriptionMapping
    public Flux<OrderResponse> subscriptionOrder(@Argument String id) {
        return subscriptionPublisher.getOrderUpdateFlux()
                .filter(o -> id.equals(o.getId()));
    }

    @SubscriptionMapping
    public Flux<OrderResponse> subscriptionDispatcher() {
        return subscriptionPublisher.getOrderUpdateFlux();
    }

    @SubscriptionMapping
    public Flux<ChatMessageOutputResponse> subscriptionNewMessage(@Argument String order) {
        return subscriptionPublisher.getChatMessageFlux()
                .filter(m -> order.equals(m.getOrderId()));
    }

    @SubscriptionMapping
    public Flux<RiderResponse> riderUpdated() {
        return subscriptionPublisher.getRiderUpdatedFlux();
    }
}
