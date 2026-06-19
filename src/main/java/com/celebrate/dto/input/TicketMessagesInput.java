package com.celebrate.dto.input;

import lombok.Data;

@Data
public class TicketMessagesInput {
    private String ticket;
    private Integer page;
    private Integer limit;
}
