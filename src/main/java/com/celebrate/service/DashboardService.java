package com.celebrate.service;

import com.celebrate.repository.*;
import com.celebrate.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final RestaurantRepository restaurantRepository;
    private final RiderRepository riderRepository;
    private final OrderRepository orderRepository;

    public Map<String, Object> getDashboardUsers() {
        SecurityUtil.requireRole("ADMIN");
        long usersCount = userRepository.countByUserType("USER");
        long vendorsCount = ownerRepository.countByUserType("VENDOR");
        long restaurantsCount = restaurantRepository.countByOwnerIdIsNotNull();
        long ridersCount = riderRepository.count();
        return Map.of(
                "usersCount", usersCount,
                "vendorsCount", vendorsCount,
                "restaurantsCount", restaurantsCount,
                "ridersCount", ridersCount
        );
    }

    public Map<String, Object> getDashboardUsersByYear(int year) {
        SecurityUtil.requireRole("ADMIN");
        // Return 12-month arrays (placeholder with zeroes — real impl needs date-grouped queries)
        List<Integer> zeros = List.of(0,0,0,0,0,0,0,0,0,0,0,0);
        return Map.of(
                "usersCount", zeros,
                "vendorsCount", zeros,
                "restaurantsCount", zeros,
                "ridersCount", zeros,
                "percentageChange", Map.of(
                        "usersPercent", 0.0,
                        "vendorsPercent", 0.0,
                        "restaurantsPercent", 0.0,
                        "ridersPercent", 0.0
                )
        );
    }

    public List<Map<String, Object>> getDashboardOrdersByType() {
        SecurityUtil.requireRole("ADMIN");
        long delivery = orderRepository.findAll().stream().filter(o -> !Boolean.TRUE.equals(o.getIsPickedUp())).count();
        long pickup = orderRepository.findAll().stream().filter(o -> Boolean.TRUE.equals(o.getIsPickedUp())).count();
        return List.of(
                Map.of("value", delivery, "label", "Delivery"),
                Map.of("value", pickup, "label", "Pickup")
        );
    }

    public List<Map<String, Object>> getDashboardSalesByType() {
        SecurityUtil.requireRole("ADMIN");
        double codTotal = orderRepository.findAll().stream()
                .filter(o -> "COD".equalsIgnoreCase(o.getPaymentMethod()))
                .mapToDouble(o -> o.getOrderAmount() != null ? o.getOrderAmount() : 0.0).sum();
        double cardTotal = orderRepository.findAll().stream()
                .filter(o -> o.getPaymentMethod() != null && !o.getPaymentMethod().equalsIgnoreCase("COD"))
                .mapToDouble(o -> o.getOrderAmount() != null ? o.getOrderAmount() : 0.0).sum();
        return List.of(
                Map.of("value", codTotal, "label", "Cash on Delivery"),
                Map.of("value", cardTotal, "label", "Card")
        );
    }

    public Map<String, Object> getDashboardTotal(String startingDate, String endingDate, String restaurantId) {
        long totalOrders = orderRepository.countByRestaurantId(restaurantId);
        double totalSales = orderRepository.findByRestaurantId(restaurantId).stream()
                .mapToDouble(o -> o.getOrderAmount() != null ? o.getOrderAmount() : 0.0).sum();
        return Map.of("totalOrders", totalOrders, "totalSales", totalSales);
    }

    public Map<String, Object> getDashboardSales(String startingDate, String endingDate, String restaurantId) {
        // Simplified: return order amounts grouped by day
        return Map.of("orders", List.of());
    }

    public Map<String, Object> getDashboardOrders(String startingDate, String endingDate, String restaurantId) {
        return Map.of("orders", List.of());
    }

    public Map<String, Object> getRestaurantDashboardOrdersSalesStats(String restaurantId, String startingDate, String endingDate, String dateKeyword) {
        List<com.celebrate.entity.OrderEntity> orders = orderRepository.findByRestaurantId(restaurantId);
        long totalOrders = orders.size();
        double totalSales = orders.stream().mapToDouble(o -> o.getOrderAmount() != null ? o.getOrderAmount() : 0.0).sum();
        long codOrders = orders.stream().filter(o -> "COD".equalsIgnoreCase(o.getPaymentMethod())).count();
        long cardOrders = orders.stream().filter(o -> o.getPaymentMethod() != null && !o.getPaymentMethod().equalsIgnoreCase("COD")).count();
        return Map.of(
                "totalOrders", totalOrders,
                "totalSales", totalSales,
                "totalCODOrders", codOrders,
                "totalCardOrders", cardOrders
        );
    }

    public Map<String, Object> getRestaurantDashboardSalesOrderCountDetailsByYear(String restaurantId, int year) {
        List<Float> salesAmount = List.of(0f,0f,0f,0f,0f,0f,0f,0f,0f,0f,0f,0f);
        List<Integer> ordersCount = List.of(0,0,0,0,0,0,0,0,0,0,0,0);
        return Map.of("salesAmount", salesAmount, "ordersCount", ordersCount);
    }

    public Map<String, Object> getDashboardOrderSalesDetailsByPaymentMethod(String restaurantId, String startingDate, String endingDate) {
        return Map.of("all", List.of(), "cod", List.of(), "card", List.of());
    }

    public Map<String, Object> metricsGeneral() {
        return Map.of(
                "excellence", "A",
                "topgun", "B",
                "experience", "C",
                "skydiver", "D",
                "rider", "E",
                "haha", "F",
                "hehe", "G",
                "huhu", "H",
                "yoyo", "I",
                "turu", "J"
        );
    }

    public Map<String, Object> getVendorDashboardStatsCardDetails(String vendorId, String dateKeyword, String startingDate, String endingDate) {
        List<com.celebrate.entity.RestaurantEntity> restaurants = restaurantRepository.findByOwnerId(vendorId);
        int totalOrders = 0;
        double totalSales = 0.0;
        for (com.celebrate.entity.RestaurantEntity r : restaurants) {
            List<com.celebrate.entity.OrderEntity> orders = orderRepository.findByRestaurantId(r.getId());
            totalOrders += orders.size();
            totalSales += orders.stream().mapToDouble(o -> o.getOrderAmount() != null ? o.getOrderAmount() : 0.0).sum();
        }
        return Map.of(
                "totalRestaurants", restaurants.size(),
                "totalOrders", totalOrders,
                "totalSales", totalSales,
                "totalDeliveries", 0
        );
    }

    public Map<String, Object> getVendorDashboardGrowthDetailsByYear(String vendorId, int year) {
        return Map.of(
                "totalRestaurants", List.of(0,0,0,0,0,0,0,0,0,0,0,0),
                "totalOrders", List.of(0,0,0,0,0,0,0,0,0,0,0,0),
                "totalSales", List.of(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0)
        );
    }

    public Map<String, Object> getLiveMonitorData(String restaurantId, String dateKeyword, String startingDate, String endingDate) {
        return Map.of(
                "online_stores", 0,
                "cancelled_orders", 0,
                "delayed_orders", 0,
                "ratings", 0
        );
    }

    public List<Map<String, Object>> getStoreDetailsByVendorId(String vendorId, String dateKeyword, String startingDate, String endingDate) {
        return restaurantRepository.findByOwnerId(vendorId).stream().map(r -> {
            List<com.celebrate.entity.OrderEntity> orders = orderRepository.findByRestaurantId(r.getId());
            double sales = orders.stream().mapToDouble(o -> o.getOrderAmount() != null ? o.getOrderAmount() : 0.0).sum();
            long pickups = orders.stream().filter(o -> Boolean.TRUE.equals(o.getIsPickedUp())).count();
            long deliveries = orders.stream().filter(o -> !Boolean.TRUE.equals(o.getIsPickedUp())).count();
            return Map.<String, Object>of(
                    "id", r.getId(),
                    "restaurantName", r.getName(),
                    "totalOrders", (int) orders.size(),
                    "totalSales", sales,
                    "pickUpCount", (int) pickups,
                    "deliveryCount", (int) deliveries
            );
        }).toList();
    }
}
