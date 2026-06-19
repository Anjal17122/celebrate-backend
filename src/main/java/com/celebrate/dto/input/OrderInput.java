package com.celebrate.dto.input;

import lombok.Data;

import java.util.List;

@Data
public class OrderInput {
    private String food;
    private Integer quantity;
    private String variation;
    private List<AddonsInput> addons;
    private String specialInstructions;
}
