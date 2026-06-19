package com.celebrate.controller;

import com.celebrate.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @QueryMapping("getDashboardUsers")
    public Map<String, Object> getDashboardUsers() {
        return dashboardService.getDashboardUsers();
    }

    @QueryMapping("getDashboardUsersByYear")
    public Map<String, Object> getDashboardUsersByYear(@Argument int year) {
        return dashboardService.getDashboardUsersByYear(year);
    }

    @QueryMapping("getDashboardOrdersByType")
    public List<Map<String, Object>> getDashboardOrdersByType() {
        return dashboardService.getDashboardOrdersByType();
    }

    @QueryMapping("getDashboardSalesByType")
    public List<Map<String, Object>> getDashboardSalesByType() {
        return dashboardService.getDashboardSalesByType();
    }

    @QueryMapping("getDashboardTotal")
    public Map<String, Object> getDashboardTotal(
            @Argument("starting_date") String startingDate,
            @Argument("ending_date") String endingDate,
            @Argument("restaurant") String restaurantId) {
        return dashboardService.getDashboardTotal(startingDate, endingDate, restaurantId);
    }

    @QueryMapping("getDashboardSales")
    public Map<String, Object> getDashboardSales(
            @Argument("starting_date") String startingDate,
            @Argument("ending_date") String endingDate,
            @Argument("restaurant") String restaurantId) {
        return dashboardService.getDashboardSales(startingDate, endingDate, restaurantId);
    }

    @QueryMapping("getDashboardOrders")
    public Map<String, Object> getDashboardOrders(
            @Argument("starting_date") String startingDate,
            @Argument("ending_date") String endingDate,
            @Argument("restaurant") String restaurantId) {
        return dashboardService.getDashboardOrders(startingDate, endingDate, restaurantId);
    }

    @QueryMapping("getRestaurantDashboardOrdersSalesStats")
    public Map<String, Object> getRestaurantDashboardOrdersSalesStats(
            @Argument("restaurant") String restaurantId,
            @Argument("starting_date") String startingDate,
            @Argument("ending_date") String endingDate,
            @Argument String dateKeyword) {
        return dashboardService.getRestaurantDashboardOrdersSalesStats(restaurantId, startingDate, endingDate, dateKeyword);
    }

    @QueryMapping("getRestaurantDashboardSalesOrderCountDetailsByYear")
    public Map<String, Object> getRestaurantDashboardSalesOrderCountDetailsByYear(
            @Argument("restaurant") String restaurantId,
            @Argument int year) {
        return dashboardService.getRestaurantDashboardSalesOrderCountDetailsByYear(restaurantId, year);
    }

    @QueryMapping("getDashboardOrderSalesDetailsByPaymentMethod")
    public Map<String, Object> getDashboardOrderSalesDetailsByPaymentMethod(
            @Argument("restaurant") String restaurantId,
            @Argument("starting_date") String startingDate,
            @Argument("ending_date") String endingDate) {
        return dashboardService.getDashboardOrderSalesDetailsByPaymentMethod(restaurantId, startingDate, endingDate);
    }

    @MutationMapping
    public Map<String, Object> metricsGeneral() {
        return dashboardService.metricsGeneral();
    }

    @QueryMapping("getVendorDashboardStatsCardDetails")
    public Map<String, Object> getVendorDashboardStatsCardDetails(
            @Argument String vendorId,
            @Argument String dateKeyword,
            @Argument("starting_date") String startingDate,
            @Argument("ending_date") String endingDate) {
        return dashboardService.getVendorDashboardStatsCardDetails(vendorId, dateKeyword, startingDate, endingDate);
    }

    @QueryMapping("getVendorDashboardGrowthDetailsByYear")
    public Map<String, Object> getVendorDashboardGrowthDetailsByYear(
            @Argument String vendorId,
            @Argument int year) {
        return dashboardService.getVendorDashboardGrowthDetailsByYear(vendorId, year);
    }

    @QueryMapping("getLiveMonitorData")
    public Map<String, Object> getLiveMonitorData(
            @Argument String id,
            @Argument String dateKeyword,
            @Argument("starting_date") String startingDate,
            @Argument("ending_date") String endingDate) {
        return dashboardService.getLiveMonitorData(id, dateKeyword, startingDate, endingDate);
    }

    @QueryMapping("getStoreDetailsByVendorId")
    public List<Map<String, Object>> getStoreDetailsByVendorId(
            @Argument String id,
            @Argument String dateKeyword,
            @Argument("starting_date") String startingDate,
            @Argument("ending_date") String endingDate) {
        return dashboardService.getStoreDetailsByVendorId(id, dateKeyword, startingDate, endingDate);
    }
}
