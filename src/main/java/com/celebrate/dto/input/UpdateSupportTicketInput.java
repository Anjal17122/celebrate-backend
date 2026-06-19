package com.celebrate.dto.input;

import lombok.Data;

@Data
public class UpdateSupportTicketInput {
    private String ticketId;
    private String status;
}
