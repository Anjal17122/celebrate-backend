package com.celebrate.service;

import com.celebrate.dto.input.ZoneInput;
import com.celebrate.dto.response.ZoneResponse;
import com.celebrate.entity.ZoneEntity;
import com.celebrate.exception.*;
import com.celebrate.mapper.ZoneMapper;
import com.celebrate.repository.ZoneRepository;
import com.celebrate.security.SecurityUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ZoneService {

    private final ZoneRepository zoneRepository;
    private final ZoneMapper zoneMapper;
    private final ObjectMapper objectMapper;

    public List<ZoneResponse> getAllZones() {
        return zoneRepository.findAll().stream().map(zoneMapper::toResponse).toList();
    }

    public ZoneResponse getZone(String id) {
        return zoneMapper.toResponse(zoneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Zone", id)));
    }

    @Transactional
    public ZoneResponse createZone(ZoneInput input) {
        SecurityUtil.requireRole("ADMIN");


        ZoneEntity zone = ZoneEntity.builder()
                .title(input.getTitle())
                .description(input.getDescription())
                .locationCoordinates(input.getCoordinates())
                .isActive(true)
                .build();

        return zoneMapper.toResponse(zoneRepository.save(zone));
    }

    @Transactional
    public ZoneResponse editZone(ZoneInput input) {
        SecurityUtil.requireRole("ADMIN");
        ZoneEntity zone = zoneRepository.findById(input.getId())
                .orElseThrow(() -> new NotFoundException("Zone", input.getId()));

        if (input.getTitle() != null) zone.setTitle(input.getTitle());
        if (input.getDescription() != null) zone.setDescription(input.getDescription());
        if (input.getCoordinates() != null) {
            zone.setLocationCoordinates(input.getCoordinates());
//            try {
//                zone.setLocationCoordinates(objectMapper.writeValueAsString(input.getCoordinates()));
//            } catch (Exception e) {
//                throw new BadRequestException("Invalid zone coordinates.");
//            }
        }

        return zoneMapper.toResponse(zoneRepository.save(zone));
    }

    @Transactional
    public ZoneResponse deleteZone(String id) {
        SecurityUtil.requireRole("ADMIN");
        ZoneEntity zone = zoneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Zone", id));
        zoneRepository.delete(zone);
        return zoneMapper.toResponse(zone);
    }
}
