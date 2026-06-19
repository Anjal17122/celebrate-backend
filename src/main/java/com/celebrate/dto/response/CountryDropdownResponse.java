package com.celebrate.dto.response;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CountryDropdownResponse {
    private String key;
    private String label;
    private String value;
}
