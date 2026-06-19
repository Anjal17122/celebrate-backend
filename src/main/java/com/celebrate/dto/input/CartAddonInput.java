package com.celebrate.dto.input;

import lombok.Data;
import java.util.List;

@Data
public class CartAddonInput {
    private String _id;
    private List<String> options;
}
