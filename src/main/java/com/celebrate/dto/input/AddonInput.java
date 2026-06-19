package com.celebrate.dto.input;

import lombok.Data;

import java.util.List;

@Data
public class AddonInput {
    private String restaurant;
    private List<CreateAddonInput> addons;
}
