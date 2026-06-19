package com.celebrate.dto.input;

import lombok.Data;

@Data
public class SingleUserSupportTicketsInput {
    private String userId;
    private FiltersInput filters;
}
