package com.celebrate.service;

import com.celebrate.dto.response.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class SubscriptionPublisher {

    private final Sinks.Many<SubscriptionOrdersResponse> orderSink =
            Sinks.many().multicast().onBackpressureBuffer();

    private final Sinks.Many<SubscriptionZoneOrdersResponse> zoneOrderSink =
            Sinks.many().multicast().onBackpressureBuffer();

    private final Sinks.Many<OrderResponse> orderUpdateSink =
            Sinks.many().multicast().onBackpressureBuffer();

    private final Sinks.Many<RiderResponse> riderLocationSink =
            Sinks.many().multicast().onBackpressureBuffer();

    private final Sinks.Many<RiderResponse> riderUpdatedSink =
            Sinks.many().multicast().onBackpressureBuffer();

    private final Sinks.Many<ChatMessageOutputResponse> chatMessageSink =
            Sinks.many().multicast().onBackpressureBuffer();

    public void publishOrderEvent(SubscriptionOrdersResponse payload) {
        orderSink.tryEmitNext(payload);
    }

    public void publishZoneOrder(SubscriptionZoneOrdersResponse payload) {
        zoneOrderSink.tryEmitNext(payload);
    }

    public void publishOrderUpdate(OrderResponse order) {
        orderUpdateSink.tryEmitNext(order);
    }

    public void publishRiderLocation(RiderResponse rider) {
        riderLocationSink.tryEmitNext(rider);
    }

    public void publishRiderUpdated(RiderResponse rider) {
        riderUpdatedSink.tryEmitNext(rider);
    }

    public void publishChatMessage(ChatMessageOutputResponse message) {
        chatMessageSink.tryEmitNext(message);
    }

    public Flux<SubscriptionOrdersResponse> getOrderFlux() {
        return orderSink.asFlux();
    }

    public Flux<SubscriptionZoneOrdersResponse> getZoneOrderFlux() {
        return zoneOrderSink.asFlux();
    }

    public Flux<OrderResponse> getOrderUpdateFlux() {
        return orderUpdateSink.asFlux()
                .doOnNext(order -> System.out.println("DEBUG: Received order update: " + order.toString()))
                .doOnError(error -> System.err.println("DEBUG: Error in stream: " + error.getMessage()));
    }

    public Flux<RiderResponse> getRiderLocationFlux() {
        return riderLocationSink.asFlux();
    }

    public Flux<RiderResponse> getRiderUpdatedFlux() {
        return riderUpdatedSink.asFlux();
    }

    public Flux<ChatMessageOutputResponse> getChatMessageFlux() {
        return chatMessageSink.asFlux();
    }
}
