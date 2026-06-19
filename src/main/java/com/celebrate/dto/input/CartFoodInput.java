package com.celebrate.dto.input;

import lombok.Data;

@Data
public class CartFoodInput {
    private String id;
    private CartVariationInput variation;

    public void set_id(String id) { this.id = id; }
    public String get_id() { return id; }
}
