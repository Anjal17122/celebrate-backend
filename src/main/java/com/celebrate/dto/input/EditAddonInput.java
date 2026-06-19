package com.celebrate.dto.input;

import lombok.Data;

@Data
public class EditAddonInput {
    private String restaurant;
    private CreateAddonInput addons;
}
