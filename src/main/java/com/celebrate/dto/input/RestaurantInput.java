package com.celebrate.dto.input;

import lombok.Data;

import java.util.List;

@Data
public class RestaurantInput {
    private String name;
    private String username;
    private String password;
    private String image;
    private String address;
    private Integer deliveryTime;
    private Integer minimumOrder;
    private Float salesTax;
    private String shopType;
    private List<String> cuisines;
    private String restaurantUrl;
    private String phone;
    private String logo;
    private BussinessDetailsInput bussinessDetails;
}
