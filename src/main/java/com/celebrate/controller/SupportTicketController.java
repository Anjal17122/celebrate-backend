package com.celebrate.controller;

import com.celebrate.dto.input.*;
import com.celebrate.dto.response.*;
import com.celebrate.service.SupportTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SupportTicketController {

    private final SupportTicketService supportTicketService;

    @QueryMapping("getAllSupportTickets")
    public Map<String, Object> getAllSupportTickets(@Argument FiltersInput input) {
        return supportTicketService.getAllSupportTickets(input);
    }

    @QueryMapping("getSingleSupportTicket")
    public SupportTicketResponse getSingleSupportTicket(@Argument("ticketId") String ticketId) {
        return supportTicketService.getSingleSupportTicket(ticketId);
    }

    @QueryMapping("getSingleUserSupportTickets")
    public Map<String, Object> getSingleUserSupportTickets(@Argument SingleUserSupportTicketsInput input) {
        return supportTicketService.getSingleUserSupportTickets(input);
    }

    @QueryMapping("getTicketMessages")
    public Map<String, Object> getTicketMessages(@Argument TicketMessagesInput input) {
        return supportTicketService.getTicketMessages(input);
    }

    @MutationMapping
    public SupportTicketResponse createSupportTicket(@Argument("ticketInput") SupportTicketInput ticketInput) {
        return supportTicketService.createSupportTicket(ticketInput);
    }

    @MutationMapping
    public SupportTicketResponse updateSupportTicketStatus(@Argument("input") UpdateSupportTicketInput input) {
        return supportTicketService.updateSupportTicketStatus(input);
    }

    @MutationMapping
    public MessageResponse createMessage(@Argument MessageInput messageInput) {
        return supportTicketService.createMessage(messageInput);
    }

    @QueryMapping("getTicketUsers")
    public Map<String, Object> getTicketUsers(@Argument FiltersInput input) {
        return supportTicketService.getTicketUsers(input);
    }

    @QueryMapping("getTicketUsersWithLatest")
    public Map<String, Object> getTicketUsersWithLatest(@Argument FiltersInput input) {
        return supportTicketService.getTicketUsersWithLatest(input);
    }
}
