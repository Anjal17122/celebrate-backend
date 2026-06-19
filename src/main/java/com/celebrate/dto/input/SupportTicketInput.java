package com.celebrate.dto.input;

import lombok.Data;

@Data
public class SupportTicketInput {
    private String title;
    private String description;
    private String category;
    private String orderId;
    private String otherDetails;
    private String userType;
}
