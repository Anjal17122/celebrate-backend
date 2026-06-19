package com.celebrate.dto.input;

import lombok.Data;

import java.util.List;

@Data
public class RestaurantProfileInput {
    private String id;
    private String name;
    private String image;
    private String logo;
    private String address;
    private String orderPrefix;
    private String username;
    private String password;
    private Integer deliveryTime;
    private Integer minimumOrder;
    private Float salesTax;
    private String shopType;
    private List<String> cuisines;
    private String restaurantUrl;
    private String phone;
    private BussinessDetailsInput bussinessDetails;
    private String owner;
    public void set_id(String _id){
        this.id = _id;
    }
}
