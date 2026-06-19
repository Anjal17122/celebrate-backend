package com.celebrate.service;

import com.celebrate.dto.input.*;
import com.celebrate.dto.response.*;
import com.celebrate.entity.*;
import com.celebrate.exception.*;
import com.celebrate.mapper.OrderMapper;
import com.celebrate.repository.*;
import com.celebrate.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final RiderRepository riderRepository;
    private final ZoneRepository zoneRepository;
    private final FoodRepository foodRepository;
    private final VariationRepository variationRepository;
    private final AddonRepository addonRepository;
    private final OptionRepository optionRepository;
    private final ReviewRepository reviewRepository;
    private final CouponRepository couponRepository;
    private final OrderMapper orderMapper;
    private final SubscriptionPublisher subscriptionPublisher;

    public OrderResponse getOrder(String id) {
        return orderMapper.toResponse(orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order", id)));
    }

    public OrderResponse getOrderById(String id) {
        return orderMapper.toResponse(orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order", id)));
    }

//    public List<OrderResponse> getOrders(Integer page, Integer limit, Integer offset) {
//        int pageNum = page != null ? Math.max(0, page - 1) : 0;
//        int pageSize = limit != null ? limit : 10;
//        return orderRepository.findAll(PageRequest.of(pageNum, pageSize, Sort.by("createdAt").descending()))
//                .stream().map(orderMapper::toResponse).toList();
//    }

    public List<OrderResponse> getOrders(Integer page, Integer limit, Integer offset) {
        String userId = SecurityUtil.getCurrentUserId();
        int pageSize = (limit != null && limit > 0) ? limit : 10;
        int offsetValue = (offset != null && offset >= 0) ? offset : 0;

        // Direct call to your custom query
        return orderRepository.findOrdersByUserIdWithPagination(userId, pageSize, offsetValue)
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    public List<OrderResponse> getUsersActiveOrders(Integer page, Integer limit, Integer offset) {
        String userId = SecurityUtil.getCurrentUserId();
        return orderRepository.findByUserId(userId).stream()
                .filter(o -> !isTerminalStatus(o.getOrderStatus()))
                .map(orderMapper::toResponse)
                .toList();
    }

    public List<OrderResponse> getUsersPastOrders(Integer page, Integer limit, Integer offset) {
        String userId = SecurityUtil.getCurrentUserId();
        return orderRepository.findByUserId(userId).stream()
                .filter(o -> isTerminalStatus(o.getOrderStatus()))
                .map(orderMapper::toResponse)
                .toList();
    }

    public List<OrderResponse> getUndeliveredOrders(Integer offset) {
        return orderRepository.findUndeliveredOrders().stream().map(orderMapper::toResponse).toList();
    }

    public List<OrderResponse> getDeliveredOrders(Integer offset) {
        return orderRepository.findDeliveredOrders().stream().map(orderMapper::toResponse).toList();
    }

    public List<OrderResponse> getAllOrders(Integer page) {
        int pageNum = page != null ? Math.max(0, page - 1) : 0;
        return orderRepository.findAll(PageRequest.of(pageNum, 20, Sort.by("createdAt").descending()))
                .stream().map(orderMapper::toResponse).toList();
    }

    public List<OrderResponse> getAllOrdersWithoutPagination(String dateKeyword, String startingDate, String endingDate, Integer page, Integer limit) {
        return orderRepository.findAll(Sort.by("createdAt").descending())
                .stream().map(orderMapper::toResponse).toList();
    }

    public Map<String, Object> getAllOrdersPaginated(Integer page, Integer rows, String dateKeyword,
                                                      String startingDate, String endingDate,
                                                      List<String> orderStatus, String search) {
        int pageNum = page != null ? Math.max(0, page - 1) : 0;
        int pageSize = rows != null ? rows : 10;
        Page<OrderEntity> result = orderRepository.findAllPaginated(
                orderStatus, search, PageRequest.of(pageNum, pageSize, Sort.by("createdAt").descending()));

        return Map.of(
                "orders", result.getContent().stream().map(orderMapper::toResponse).toList(),
                "totalCount", result.getTotalElements(),
                "totalPages", result.getTotalPages(),
                "currentPage", pageNum + 1,
                "prevPage", pageNum > 0 ? pageNum : null,
                "nextPage", result.hasNext() ? pageNum + 2 : null
        );
    }

    public Map<String, Object> getOrdersByRestId(String restaurantId, Integer page, Integer rows, String search) {
        int pageNum = page != null ? Math.max(0, page - 1) : 0;
        int pageSize = rows != null ? rows : 10;
        Page<OrderEntity> result = orderRepository.findByRestaurantWithSearch(
                restaurantId, search, PageRequest.of(pageNum, pageSize, Sort.by("createdAt").descending()));
        return Map.of(
                "orders", result.getContent().stream().map(orderMapper::toResponse).toList(),
                "totalCount", result.getTotalElements(),
                "totalPages", result.getTotalPages(),
                "currentPage", pageNum + 1,
                "prevPage", pageNum > 0 ? pageNum : null,
                "nextPage", result.hasNext() ? pageNum + 2 : null
        );
    }

    public List<OrderResponse> getOrdersByRestIdWithoutPagination(String restaurantId, String search) {
        return orderRepository.findByRestaurantId(restaurantId).stream().map(orderMapper::toResponse).toList();
    }

    public Map<String, Object> getOrdersByUser(String userId, Integer page, Integer limit) {
        int pageNum = page != null ? Math.max(0, page - 1) : 0;
        int pageSize = limit != null ? limit : 10;
        Page<OrderEntity> result = orderRepository.findByUserId(userId, PageRequest.of(pageNum, pageSize, Sort.by("createdAt").descending()));
        return Map.of(
                "orders", result.getContent().stream().map(orderMapper::toResponse).toList(),
                "totalCount", result.getTotalElements(),
                "totalPages", result.getTotalPages(),
                "currentPage", pageNum + 1,
                "prevPage", pageNum > 0 ? pageNum : null,
                "nextPage", result.hasNext() ? pageNum + 2 : null
        );
    }

    public List<OrderResponse> getRestaurantOrders() {
        String ownerId = SecurityUtil.getCurrentUserId();
        List<RestaurantEntity> restaurants = restaurantRepository.findByOwnerId(ownerId);
        if (restaurants.isEmpty()) return List.of();
        return orderRepository.findByRestaurantId(restaurants.get(0).getId())
                .stream().map(orderMapper::toResponse).toList();
    }

    public List<OrderResponse> getRiderCompletedOrders() {
        String riderId = SecurityUtil.getCurrentUserId();
        return orderRepository.findByRiderId(riderId).stream()
                .filter(o -> "DELIVERED".equals(o.getOrderStatus()))
                .map(orderMapper::toResponse)
                .toList();
    }

    public List<OrderResponse> getAssignedOrders(String riderId) {
        String id = riderId != null ? riderId : SecurityUtil.getCurrentUserId();
        return orderRepository.findByRiderId(id).stream()
                .filter(o -> !isTerminalStatus(o.getOrderStatus()))
                .map(orderMapper::toResponse)
                .toList();
    }

    public List<OrderResponse> getRiderOrders() {
        String riderId = SecurityUtil.getCurrentUserId();
        return orderRepository.findByRiderId(riderId).stream().map(orderMapper::toResponse).toList();
    }

    public List<OrderResponse> getUnassignedOrdersByZone() {
        return orderRepository.findByRiderIsNull().stream()
                .filter(o -> !isTerminalStatus(o.getOrderStatus()))
                .map(orderMapper::toResponse)
                .toList();
    }

    public Map<String, Object> getActiveOrders(String restaurantId, Integer page, Integer rowsPerPage, String search, List<String> actions) {
        int pageNum = page != null ? Math.max(0, page - 1) : 0;
        int pageSize = rowsPerPage != null ? rowsPerPage : 10;
        List<OrderEntity> activeOrders = restaurantId != null
                ? orderRepository.findActiveByRestaurantId(restaurantId)
                : orderRepository.findUndeliveredOrders();
        return Map.of(
                "orders", activeOrders.stream().map(orderMapper::toResponse).toList(),
                "totalCount", activeOrders.size(),
                "totalPages", 1,
                "currentPage", 1
        );
    }

    public int getOrderCount(String restaurantId) {
        return orderRepository.countByRestaurantId(restaurantId);
    }

    public Map<String, Object> getOrdersByDateRange(String startingDate, String endingDate, String restaurantId) {
        List<OrderEntity> orders = orderRepository.findByRestaurantId(restaurantId);
        double totalCOD = orders.stream()
                .filter(o -> "COD".equalsIgnoreCase(o.getPaymentMethod()))
                .mapToDouble(o -> o.getOrderAmount() != null ? o.getOrderAmount() : 0.0)
                .sum();
        long codCount = orders.stream()
                .filter(o -> "COD".equalsIgnoreCase(o.getPaymentMethod()))
                .count();
        return Map.of(
                "orders", orders.stream().map(orderMapper::toResponse).toList(),
                "totalAmountCashOnDelivery", totalCOD,
                "countCashOnDeliveryOrders", codCount
        );
    }

    public List<String> getOrderStatuses() {
        return List.of("PENDING", "ACCEPTED", "PREPARING", "PICKED", "DELIVERED", "CANCELLED", "CANCELLEDBYREST");
    }

    public List<String> getPaymentStatuses() {
        return List.of("PENDING", "COMPLETED", "FAILED", "REFUNDED");
    }

    @Transactional
    public OrderResponse placeOrder(String restaurantId, List<OrderInput> orderInput,
                                     String paymentMethod, String couponCode,
                                     AddressInput addressInput, Float tipping,
                                     String orderDate, Boolean isPickedUp,
                                     Float taxationAmount, Float deliveryCharges,
                                     String instructions) {
        String userId = SecurityUtil.getCurrentUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant", restaurantId));

        if (!Boolean.TRUE.equals(restaurant.getIsAvailable())) {
            throw new BadRequestException("This restaurant is currently unavailable.");
        }

        double orderAmount = 0.0;
        double discountAmount = 0.0;

        // Coupon validation
        if (couponCode != null) {
            CouponEntity coupon = couponRepository.findByTitleAndRestaurantIsNull(couponCode)
                    .orElseGet(() -> couponRepository.findByTitleAndRestaurantId(couponCode, restaurantId).orElse(null));
            if (coupon != null && Boolean.TRUE.equals(coupon.getEnabled())) {
                discountAmount = coupon.getDiscount();
            }
        }

        String prefix = restaurant.getOrderPrefix() != null ? restaurant.getOrderPrefix() : "ORD";
        String orderId = prefix + "-" + System.currentTimeMillis();

        ZoneEntity zone = restaurant.getZone();

        OrderEntity order = OrderEntity.builder()
                .orderId(orderId)
                .paymentMethod(paymentMethod)
                .paidAmount(0.0)
                .orderAmount(orderAmount)
                .discountAmount(discountAmount)
                .status(false)
                .paymentStatus("PENDING")
                .orderStatus("PENDING")
                .isActive(true)
                .deliveryCharges(deliveryCharges != null ? deliveryCharges.doubleValue() : 0.0)
                .tipping(tipping != null ? tipping.doubleValue() : 0.0)
                .taxationAmount(taxationAmount != null ? taxationAmount.doubleValue() : 0.0)
                .orderDate(orderDate)
                .isPickedUp(isPickedUp != null ? isPickedUp : false)
                .isRinged(false)
                .isRiderRinged(false)
                .instructions(instructions)
                .deliveryLat(addressInput.getLatitude())
                .deliveryLng(addressInput.getLongitude())
                .deliveryAddress(addressInput.getDeliveryAddress())
                .deliveryDetails(addressInput.getDetails())
                .deliveryLabel(addressInput.getLabel())
                .couponCode(couponCode)
                .restaurant(restaurant)
                .user(user)
                .zone(zone)
                .build();

        OrderEntity savedOrder = orderRepository.save(order);

        // Build order items
        double computedAmount = 0.0;
        List<OrderItemEntity> orderItems = new ArrayList<>();
        for (OrderInput item : orderInput) {
            FoodEntity food = foodRepository.findById(item.getFood())
                    .orElseThrow(() -> new NotFoundException("Food", item.getFood()));
            VariationEntity variation = variationRepository.findById(item.getVariation())
                    .orElseThrow(() -> new NotFoundException("Variation", item.getVariation()));

            ItemVariationEntity itemVariation = ItemVariationEntity.builder()
                    .variationId(variation.getId())
                    .title(variation.getTitle())
                    .price(variation.getPrice())
                    .discounted(variation.getDiscounted() != null ? variation.getDiscounted() : 0.0)
                    .build();

            OrderItemEntity orderItem = OrderItemEntity.builder()
                    .order(savedOrder)
                    .foodId(food.getId())
                    .title(food.getTitle())
                    .description(food.getDescription())
                    .image(food.getImage())
                    .quantity(item.getQuantity())
                    .specialInstructions(item.getSpecialInstructions())
                    .isActive(true)
                    .variation(itemVariation)
                    .build();

            itemVariation.setOrderItem(orderItem);

            double itemPrice = (variation.getDiscounted() != null && variation.getDiscounted() > 0
                    ? variation.getDiscounted() : variation.getPrice()) * item.getQuantity();

            // Process addons
            List<ItemAddonEntity> itemAddons = new ArrayList<>();
            if (item.getAddons() != null) {
                for (AddonsInput addonInput : item.getAddons()) {
                    AddonEntity addon = addonRepository.findById(addonInput.getId()).orElse(null);
                    if (addon == null) continue;

                    ItemAddonEntity itemAddon = ItemAddonEntity.builder()
                            .addonId(addon.getId())
                            .title(addon.getTitle())
                            .description(addon.getDescription())
                            .quantityMinimum(addon.getQuantityMinimum())
                            .quantityMaximum(addon.getQuantityMaximum())
                            .orderItem(orderItem)
                            .build();

                    if (addonInput.getOptions() != null) {
                        List<ItemOptionEntity> itemOptions = new ArrayList<>();
                        for (String optionId : addonInput.getOptions()) {
                            OptionEntity option = optionRepository.findById(optionId).orElse(null);
                            if (option != null) {
                                ItemOptionEntity itemOption = ItemOptionEntity.builder()
                                        .optionId(option.getId())
                                        .title(option.getTitle())
                                        .description(option.getDescription())
                                        .price(option.getPrice())
                                        .itemAddon(itemAddon)
                                        .build();
                                itemOptions.add(itemOption);
                                itemPrice += option.getPrice();
                            }
                        }
                        itemAddon.setOptions(itemOptions);
                    }
                    itemAddons.add(itemAddon);
                }
            }
            orderItem.setAddons(itemAddons);
            orderItems.add(orderItem);

            computedAmount += itemPrice;
        }

        savedOrder.setItems(orderItems);

        savedOrder.setOrderAmount(computedAmount - discountAmount);
        OrderEntity getSavedOrder = orderRepository.saveAndFlush(savedOrder);
        OrderResponse orderResponse = orderMapper.toResponse(getSavedOrder);

        SubscriptionOrdersResponse sub = SubscriptionOrdersResponse.builder()
                .restaurantId(restaurantId)
                .userId(userId)
                .order(orderResponse)
                .origin("new")
                .build();
        subscriptionPublisher.publishOrderEvent(sub);
        subscriptionPublisher.publishOrderUpdate(orderResponse);

        if (zone != null) {
            subscriptionPublisher.publishZoneOrder(SubscriptionZoneOrdersResponse.builder()
                    .zoneId(zone.getId())
                    .order(orderResponse)
                    .origin("new")
                    .build());
        }

        return orderResponse;
    }

    @Transactional
    public OrderResponse acceptOrder(String id, String time) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order", id));
        order.setOrderStatus("ACCEPTED");
        order.setAcceptedAt(LocalDateTime.now().toString());
        if (time != null) order.setPreparationTime(time);
        return publishStatusEvent(orderRepository.save(order), "status");
    }

    @Transactional
    public OrderResponse orderPickedUp(String id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order", id));
        order.setOrderStatus("PICKED");
        order.setPickedAt(LocalDateTime.now().toString());
        return publishStatusEvent(orderRepository.save(order), "status");
    }

    @Transactional
    public OrderResponse cancelOrder(String id, String reason) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order", id));
        order.setOrderStatus("CANCELLED");
        order.setReason(reason);
        order.setCancelledAt(LocalDateTime.now().toString());
        return publishStatusEvent(orderRepository.save(order), "status");
    }

    @Transactional
    public OrderResponse updateOrderStatus(String id, String status, String reason) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order", id));
        order.setOrderStatus(status);
        if (reason != null) order.setReason(reason);
        if ("DELIVERED".equals(status)) {
            order.setDeliveredAt(LocalDateTime.now().toString());
            order.setStatus(true);
            order.setPaymentStatus("COMPLETED");
        }
        return publishStatusEvent(orderRepository.save(order), "status");
    }

    private OrderResponse publishStatusEvent(OrderEntity saved, String origin) {
        OrderResponse response = orderMapper.toResponse(saved);
        String userId = saved.getUser() != null ? saved.getUser().getId() : null;
        String restaurantId = saved.getRestaurant() != null ? saved.getRestaurant().getId() : null;
        subscriptionPublisher.publishOrderEvent(SubscriptionOrdersResponse.builder()
                .restaurantId(restaurantId)
                .userId(userId)
                .order(response)
                .origin(origin)
                .build());
        subscriptionPublisher.publishOrderUpdate(response);
        return response;
    }

    @Transactional
    public OrderResponse updateOrderStatusRider(String id, String status) {
        return updateOrderStatus(id, status, null);
    }

    @Transactional
    public OrderResponse updateStatus(String id, String orderStatus) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order", id));
        order.setOrderStatus(orderStatus);
        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Transactional
    public OrderResponse assignRider(String orderId, String riderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order", orderId));
        RiderEntity rider = riderRepository.findById(riderId)
                .orElseThrow(() -> new NotFoundException("Rider", riderId));
        order.setRider(rider);
        order.setAssignedAt(LocalDateTime.now().toString());
        order.setOrderStatus("assigned");
        OrderResponse response = orderMapper.toResponse(orderRepository.save(order));
        String userId = order.getUser() != null ? order.getUser().getId() : null;
        String restaurantId = order.getRestaurant() != null ? order.getRestaurant().getId() : null;
        subscriptionPublisher.publishOrderEvent(SubscriptionOrdersResponse.builder()
                .restaurantId(restaurantId)
                .userId(userId)
                .riderId(riderId)
                .order(response)
                .origin("assigned")
                .build());
        subscriptionPublisher.publishOrderUpdate(response);
        return response;
    }

    @Transactional
    public OrderResponse assignOrder(String orderId) {
        String riderId = SecurityUtil.getCurrentUserId();
        RiderEntity rider = riderRepository.findById(riderId)
                .orElseThrow(() -> new NotFoundException("Rider", riderId));

        OrderEntity order;
        if (orderId != null) {
            order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new NotFoundException("Order", orderId));
        } else {
            // Find first unassigned order in rider's zone
            order = orderRepository.findByZoneId(rider.getZone().getId()).stream()
                    .filter(o -> o.getRider() == null && "PENDING".equals(o.getOrderStatus()))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("No available orders in your zone."));
        }
        order.setRider(rider);
        order.setAssignedAt(LocalDateTime.now().toString());
        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Transactional
    public OrderResponse updatePaymentStatus(String id, String status) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order", id));
        order.setPaymentStatus(status);
        if ("COMPLETED".equals(status)) order.setStatus(true);
        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Transactional
    public boolean orderCreatedAndPaid(String orderId, String restaurantId, List<OrderInput> orderInput) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order", orderId));
        order.setPaymentStatus("COMPLETED");
        order.setStatus(true);
        orderRepository.save(order);
        return true;
    }

    @Transactional
    public OrderResponse editOrder(String id, List<OrderInput> orderInput) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order", id));
        // Only allow editing pending orders
        if (!"PENDING".equals(order.getOrderStatus())) {
            throw new BadRequestException("Cannot edit an order that is not pending.");
        }
        // Re-build items would require deleting old items; for now return current state
        return orderMapper.toResponse(order);
    }

    @Transactional
    public OrderResponse reviewOrder(ReviewInput input) {
        String userId = SecurityUtil.getCurrentUserId();
        OrderEntity order = orderRepository.findById(input.getOrder())
                .orElseThrow(() -> new NotFoundException("Order", input.getOrder()));

        if (!order.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You can only review your own orders.");
        }
        if (!"DELIVERED".equals(order.getOrderStatus())) {
            throw new BadRequestException("You can only review delivered orders.");
        }
        if (order.getReview() != null) {
            throw new ConflictException("This order has already been reviewed.");
        }

        ReviewEntity review = ReviewEntity.builder()
                .order(order)
                .restaurant(order.getRestaurant())
                .rating(input.getRating())
                .description(input.getDescription())
                .comments(input.getComments())
                .isActive(true)
                .build();
        reviewRepository.save(review);
        return orderMapper.toResponse(orderRepository.findById(order.getId()).orElseThrow());
    }

    @Transactional
    public OrderResponse createReview(ReviewInput input) {
        return reviewOrder(input);
    }

    @Transactional
    public boolean muteRing(String orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order", orderId));
        order.setIsRinged(true);
        orderRepository.save(order);
        return true;
    }

    @Transactional
    public OrderResponse abortOrder(String id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order", id));
        order.setOrderStatus("CANCELLED");
        order.setReason("Order aborted.");
        return orderMapper.toResponse(orderRepository.save(order));
    }

    private boolean isTerminalStatus(String status) {
        return status != null && (status.equals("DELIVERED") || status.equals("CANCELLED") || status.equals("CANCELLEDBYREST"));
    }
}
