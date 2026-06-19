package com.celebrate.dto.input;

import lombok.Data;
import java.util.List;

@Data
public class CartVariationInput {
    private String id;
    private List<CartAddonInput> addons;

    public void set_id(String id) { this.id = id; }
    public String get_id() { return id; }
}
