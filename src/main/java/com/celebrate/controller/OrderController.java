package com.celebrate.controller;

import com.celebrate.dto.input.*;
import com.celebrate.dto.response.*;
import com.celebrate.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @QueryMapping
    public OrderResponse order(@Argument String id) {
        return orderService.getOrder(id);
    }

    @QueryMapping
    public OrderResponse orderDetails(@Argument String id) {
        return orderService.getOrderById(id);
    }

    @QueryMapping
    public OrderResponse orderPaypal(@Argument String id) {
        return orderService.getOrderById(id);
    }

    @QueryMapping
    public OrderResponse orderStripe(@Argument String id) {
        return orderService.getOrderById(id);
    }

    @QueryMapping
    public List<OrderResponse> orders(@Argument Integer page, @Argument Integer limit, @Argument Integer offset) {
        return orderService.getOrders(page, limit, offset);
    }

    @QueryMapping
    public List<OrderResponse> getUsersActiveOrders(@Argument Integer page, @Argument Integer limit, @Argument Integer offset) {
        return orderService.getUsersActiveOrders(page, limit, offset);
    }

    @QueryMapping
    public List<OrderResponse> getUsersPastOrders(@Argument Integer page, @Argument Integer limit, @Argument Integer offset) {
        return orderService.getUsersPastOrders(page, limit, offset);
    }

    @QueryMapping
    public List<OrderResponse> undeliveredOrders(@Argument Integer offset) {
        return orderService.getUndeliveredOrders(offset);
    }

    @QueryMapping
    public List<OrderResponse> deliveredOrders(@Argument Integer offset) {
        return orderService.getDeliveredOrders(offset);
    }

    @QueryMapping
    public List<OrderResponse> allOrders(@Argument Integer page) {
        return orderService.getAllOrders(page);
    }

    @QueryMapping
    public List<OrderResponse> allOrdersWithoutPagination(
            @Argument String dateKeyword, @Argument String starting_date,
            @Argument String ending_date, @Argument Integer page, @Argument Integer limit) {
        return orderService.getAllOrdersWithoutPagination(dateKeyword, starting_date, ending_date, page, limit);
    }

    @QueryMapping
    public Map<String, Object> allOrdersPaginated(
            @Argument Integer page, @Argument Integer rows, @Argument String dateKeyword,
            @Argument String starting_date, @Argument String ending_date,
            @Argument List<String> orderStatus, @Argument String search) {
        return orderService.getAllOrdersPaginated(page, rows, dateKeyword, starting_date, ending_date, orderStatus, search);
    }

    @QueryMapping
    public Map<String, Object> ordersByRestId(
            @Argument String restaurant, @Argument Integer page,
            @Argument Integer rows, @Argument String search) {
        return orderService.getOrdersByRestId(restaurant, page, rows, search);
    }

    @QueryMapping
    public List<OrderResponse> ordersByRestIdWithoutPagination(
            @Argument String restaurant, @Argument String search) {
        return orderService.getOrdersByRestIdWithoutPagination(restaurant, search);
    }

    @QueryMapping
    public Map<String, Object> ordersByUser(@Argument String userId, @Argument Integer page, @Argument Integer limit) {
        return orderService.getOrdersByUser(userId, page, limit);
    }

    @QueryMapping
    public List<OrderResponse> restaurantOrders() {
        return orderService.getRestaurantOrders();
    }

    @QueryMapping
    public List<OrderResponse> riderCompletedOrders() {
        return orderService.getRiderCompletedOrders();
    }

    @QueryMapping
    public List<OrderResponse> assignedOrders(@Argument String id) {
        return orderService.getAssignedOrders(id);
    }

    @QueryMapping
    public List<OrderResponse> riderOrders() {
        return orderService.getRiderOrders();
    }

    @QueryMapping
    public List<OrderResponse> unassignedOrdersByZone() {
        return orderService.getUnassignedOrdersByZone();
    }

    @QueryMapping
    public Map<String, Object> getActiveOrders(
            @Argument String restaurantId, @Argument Integer page,
            @Argument Integer rowsPerPage, @Argument String search,
            @Argument List<String> actions) {
        return orderService.getActiveOrders(restaurantId, page, rowsPerPage, search, actions);
    }

    @QueryMapping
    public int orderCount(@Argument String restaurant) {
        return orderService.getOrderCount(restaurant);
    }

    @QueryMapping
    public Map<String, Object> getOrdersByDateRange(
            @Argument String startingDate, @Argument String endingDate, @Argument String restaurant) {
        return orderService.getOrdersByDateRange(startingDate, endingDate, restaurant);
    }

    @QueryMapping
    public List<String> getOrderStatuses() {
        return orderService.getOrderStatuses();
    }

    @QueryMapping
    public List<String> getPaymentStatuses() {
        return orderService.getPaymentStatuses();
    }

    @MutationMapping
    public OrderResponse placeOrder(
            @Argument String restaurant,
            @Argument List<OrderInput> orderInput,
            @Argument String paymentMethod,
            @Argument String couponCode,
            @Argument AddressInput address,
            @Argument Float tipping,
            @Argument String orderDate,
            @Argument Boolean isPickedUp,
            @Argument Float taxationAmount,
            @Argument Float deliveryCharges,
            @Argument String instructions) {
        return orderService.placeOrder(restaurant, orderInput, paymentMethod, couponCode,
                address, tipping, orderDate, isPickedUp, taxationAmount, deliveryCharges, instructions);
    }

    @MutationMapping
    public boolean orderCreatedAndPaid(
            @Argument String orderId,
            @Argument String restaurant,
            @Argument List<OrderInput> orderInput) {
        return orderService.orderCreatedAndPaid(orderId, restaurant, orderInput);
    }

    @MutationMapping
    public OrderResponse editOrder(@Argument String _id, @Argument List<OrderInput> orderInput) {
        return orderService.editOrder(_id, orderInput);
    }

    @MutationMapping
    public OrderResponse reviewOrder(@Argument ReviewInput reviewInput) {
        return orderService.reviewOrder(reviewInput);
    }

    @MutationMapping
    public OrderResponse createReview(@Argument ReviewInput review) {
        return orderService.createReview(review);
    }

    @MutationMapping
    public OrderResponse acceptOrder(@Argument String _id, @Argument String time) {
        return orderService.acceptOrder(_id, time);
    }

    @MutationMapping
    public OrderResponse orderPickedUp(@Argument String _id) {
        return orderService.orderPickedUp(_id);
    }

    @MutationMapping
    public OrderResponse cancelOrder(@Argument String _id, @Argument String reason) {
        return orderService.cancelOrder(_id, reason);
    }

    @MutationMapping
    public OrderResponse updateOrderStatus(@Argument String id, @Argument String status, @Argument String reason) {
        return orderService.updateOrderStatus(id, status, reason);
    }

    @MutationMapping
    public OrderResponse updateOrderStatusRider(@Argument String id, @Argument String status) {
        return orderService.updateOrderStatusRider(id, status);
    }

    @MutationMapping
    public OrderResponse updateStatus(@Argument String id, @Argument String orderStatus) {
        return orderService.updateStatus(id, orderStatus);
    }

    @MutationMapping
    public OrderResponse assignRider(@Argument String id, @Argument String riderId) {
        return orderService.assignRider(id, riderId);
    }

    @MutationMapping
    public OrderResponse assignOrder(@Argument String id) {
        return orderService.assignOrder(id);
    }

    @MutationMapping
    public OrderResponse updatePaymentStatus(@Argument String id, @Argument String status) {
        return orderService.updatePaymentStatus(id, status);
    }

    @MutationMapping
    public boolean muteRing(@Argument String orderId) {
        return orderService.muteRing(orderId);
    }

    @MutationMapping
    public OrderResponse abortOrder(@Argument String id) {
        return orderService.abortOrder(id);
    }
}
