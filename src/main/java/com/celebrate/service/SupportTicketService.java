package com.celebrate.service;

import com.celebrate.dto.input.*;
import com.celebrate.dto.response.*;
import com.celebrate.entity.*;
import com.celebrate.exception.*;
import com.celebrate.mapper.SupportTicketMapper;
import com.celebrate.repository.*;
import com.celebrate.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportTicketService {

    private final SupportTicketRepository supportTicketRepository;
    private final TicketMessageRepository ticketMessageRepository;
    private final UserRepository userRepository;
    private final SupportTicketMapper supportTicketMapper;

    public Map<String, Object> getAllSupportTickets(FiltersInput input) {
        SecurityUtil.requireRole("ADMIN", "STAFF");
        int pageNum = input != null && input.getPage() != null ? Math.max(0, input.getPage() - 1) : 0;
        int pageSize = input != null && input.getLimit() != null ? input.getLimit() : 10;
        String search = input != null ? input.getSearch() : null;
        String status = input != null ? input.getStatus() : null;

        Page<SupportTicketEntity> result = supportTicketRepository.findAllFiltered(status, search,
                PageRequest.of(pageNum, pageSize, Sort.by("createdAt").descending()));

        return Map.of(
                "tickets", result.getContent().stream().map(supportTicketMapper::toResponse).toList(),
                "docsCount", result.getTotalElements(),
                "totalPages", result.getTotalPages(),
                "currentPage", pageNum + 1
        );
    }

    public SupportTicketResponse getSingleSupportTicket(String ticketId) {
        return supportTicketMapper.toResponse(supportTicketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket", ticketId)));
    }

    public Map<String, Object> getSingleUserSupportTickets(SingleUserSupportTicketsInput input) {
        int pageNum = input.getFilters() != null && input.getFilters().getPage() != null
                ? Math.max(0, input.getFilters().getPage() - 1) : 0;
        int pageSize = input.getFilters() != null && input.getFilters().getLimit() != null
                ? input.getFilters().getLimit() : 10;

        Page<SupportTicketEntity> result = supportTicketRepository.findByUserId(
                input.getUserId(), PageRequest.of(pageNum, pageSize, Sort.by("createdAt").descending()));

        return Map.of(
                "tickets", result.getContent().stream().map(supportTicketMapper::toResponse).toList(),
                "docsCount", result.getTotalElements(),
                "totalPages", result.getTotalPages(),
                "currentPage", pageNum + 1
        );
    }

    public Map<String, Object> getTicketMessages(TicketMessagesInput input) {
        int pageNum = input.getPage() != null ? Math.max(0, input.getPage() - 1) : 0;
        int pageSize = input.getLimit() != null ? input.getLimit() : 20;

        SupportTicketEntity ticket = supportTicketRepository.findById(input.getTicket())
                .orElseThrow(() -> new NotFoundException("Ticket", input.getTicket()));

        Page<TicketMessageEntity> messages = ticketMessageRepository.findByTicketId(
                input.getTicket(), PageRequest.of(pageNum, pageSize, Sort.by("createdAt").ascending()));

        return Map.of(
                "messages", messages.getContent().stream().map(m -> MessageResponse.builder()
                        .id(m.getId())
                        .senderType(m.getSenderType())
                        .content(m.getContent())
                        .isRead(m.getIsRead())
                        .ticket(m.getTicket().getId())
                        .createdAt(m.getCreatedAt() != null ? m.getCreatedAt().toString() : null)
                        .updatedAt(m.getUpdatedAt() != null ? m.getUpdatedAt().toString() : null)
                        .build()).toList(),
                "ticket", supportTicketMapper.toResponse(ticket),
                "page", pageNum + 1,
                "totalPages", messages.getTotalPages(),
                "docsCount", messages.getTotalElements()
        );
    }

    @Transactional
    public SupportTicketResponse createSupportTicket(SupportTicketInput input) {
        String userId = SecurityUtil.getCurrentUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));

        SupportTicketEntity ticket = SupportTicketEntity.builder()
                .title(input.getTitle())
                .description(input.getDescription())
                .category(input.getCategory())
                .orderId(input.getOrderId())
                .otherDetails(input.getOtherDetails())
                .status("open")
                .userType(input.getUserType())
                .user(user)
                .build();

        return supportTicketMapper.toResponse(supportTicketRepository.save(ticket));
    }

    @Transactional
    public SupportTicketResponse updateSupportTicketStatus(UpdateSupportTicketInput input) {
        SecurityUtil.requireRole("ADMIN", "STAFF");
        SupportTicketEntity ticket = supportTicketRepository.findById(input.getTicketId())
                .orElseThrow(() -> new NotFoundException("Ticket", input.getTicketId()));
        ticket.setStatus(input.getStatus());
        return supportTicketMapper.toResponse(supportTicketRepository.save(ticket));
    }

    public Map<String, Object> getTicketUsers(FiltersInput input) {
        SecurityUtil.requireRole("ADMIN", "STAFF");
        int pageNum = input != null && input.getPage() != null ? Math.max(0, input.getPage() - 1) : 0;
        int pageSize = input != null && input.getLimit() != null ? input.getLimit() : 10;

        Page<SupportTicketEntity> ticketPage = supportTicketRepository.findAll(
                PageRequest.of(pageNum, pageSize, Sort.by("createdAt").descending()));

        Set<String> seen = new LinkedHashSet<>();
        List<UserEntity> users = ticketPage.getContent().stream()
                .filter(t -> t.getUser() != null && seen.add(t.getUser().getId()))
                .map(SupportTicketEntity::getUser)
                .toList();

        return Map.of(
                "users", users.stream().map(this::toUserMap).toList(),
                "docsCount", ticketPage.getTotalElements(),
                "totalPages", ticketPage.getTotalPages(),
                "currentPage", pageNum + 1
        );
    }

    public Map<String, Object> getTicketUsersWithLatest(FiltersInput input) {
        SecurityUtil.requireRole("ADMIN", "STAFF");
        int pageNum = input != null && input.getPage() != null ? Math.max(0, input.getPage() - 1) : 0;
        int pageSize = input != null && input.getLimit() != null ? input.getLimit() : 10;

        Page<SupportTicketEntity> ticketPage = supportTicketRepository.findAll(
                PageRequest.of(pageNum, pageSize, Sort.by("createdAt").descending()));

        Map<String, SupportTicketEntity> latestByUser = new LinkedHashMap<>();
        for (SupportTicketEntity t : ticketPage.getContent()) {
            if (t.getUser() != null) {
                latestByUser.putIfAbsent(t.getUser().getId(), t);
            }
        }

        List<Map<String, Object>> usersWithTicket = latestByUser.values().stream().map(t -> {
            Map<String, Object> u = toUserMap(t.getUser());
            Map<String, Object> latestTicket = Map.of(
                    "_id", t.getId(),
                    "title", t.getTitle() != null ? t.getTitle() : "",
                    "status", t.getStatus() != null ? t.getStatus() : "",
                    "category", t.getCategory() != null ? t.getCategory() : "",
                    "createdAt", t.getCreatedAt() != null ? t.getCreatedAt().toString() : ""
            );
            Map<String, Object> result = new HashMap<>(u);
            result.put("latestTicket", latestTicket);
            return result;
        }).toList();

        return Map.of(
                "users", usersWithTicket,
                "docsCount", ticketPage.getTotalElements(),
                "totalPages", ticketPage.getTotalPages(),
                "currentPage", pageNum + 1
        );
    }

    private Map<String, Object> toUserMap(UserEntity u) {
        Map<String, Object> m = new HashMap<>();
        m.put("_id", u.getId());
        m.put("name", u.getName() != null ? u.getName() : "");
        m.put("email", u.getEmail() != null ? u.getEmail() : "");
        m.put("phone", u.getPhone());
        m.put("isActive", u.getIsActive());
        m.put("userType", "user");
        return m;
    }

    @Transactional
    public MessageResponse createMessage(MessageInput input) {
        String userId = SecurityUtil.getCurrentUserId();
        SupportTicketEntity ticket = supportTicketRepository.findById(input.getTicket())
                .orElseThrow(() -> new NotFoundException("Ticket", input.getTicket()));

        String senderType = SecurityUtil.hasRole("ADMIN") || SecurityUtil.hasRole("STAFF") ? "admin" : "user";

        TicketMessageEntity message = TicketMessageEntity.builder()
                .content(input.getContent())
                .senderType(senderType)
                .isRead(false)
                .ticket(ticket)
                .build();

        TicketMessageEntity saved = ticketMessageRepository.save(message);

        return MessageResponse.builder()
                .id(saved.getId())
                .senderType(saved.getSenderType())
                .content(saved.getContent())
                .isRead(saved.getIsRead())
                .ticket(saved.getTicket().getId())
                .createdAt(saved.getCreatedAt() != null ? saved.getCreatedAt().toString() : null)
                .updatedAt(saved.getUpdatedAt() != null ? saved.getUpdatedAt().toString() : null)
                .build();
    }
}
