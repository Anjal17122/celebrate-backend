package com.celebrate.mapper;

import com.celebrate.dto.response.MessageResponse;
import com.celebrate.dto.response.SupportTicketResponse;
import com.celebrate.entity.SupportTicketEntity;
import com.celebrate.entity.TicketMessageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface SupportTicketMapper {

    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    SupportTicketResponse toResponse(SupportTicketEntity entity);

    List<SupportTicketResponse> toResponseList(List<SupportTicketEntity> entities);

    @Mapping(source = "ticket.id", target = "ticket")
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    MessageResponse toMessageResponse(TicketMessageEntity entity);

    List<MessageResponse> toMessageResponseList(List<TicketMessageEntity> entities);
}
