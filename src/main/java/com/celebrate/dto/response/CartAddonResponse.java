package com.celebrate.dto.response;

import lombok.*;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CartAddonResponse {
    private String id;
    private List<OptionResponse> options;
    private String title;
    private String description;
    private Integer quantityMinimum;
    private Integer quantityMaximum;
    public String get_id() { return id; }
}
