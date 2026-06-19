package com.celebrate.service;

import com.celebrate.dto.input.SectionInput;
import com.celebrate.dto.response.SectionResponse;
import com.celebrate.entity.RestaurantEntity;
import com.celebrate.entity.SectionEntity;
import com.celebrate.exception.*;
import com.celebrate.mapper.SectionMapper;
import com.celebrate.repository.RestaurantRepository;
import com.celebrate.repository.SectionRepository;
import com.celebrate.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;
    private final RestaurantRepository restaurantRepository;
    private final SectionMapper sectionMapper;

    public List<SectionResponse> getAllSections() {
        return sectionRepository.findAll().stream().map(sectionMapper::toResponse).toList();
    }

    @Transactional
    public SectionResponse createSection(SectionInput input) {
        SecurityUtil.requireRole("ADMIN");
        List<RestaurantEntity> restaurants = new ArrayList<>();
        if (input.getRestaurants() != null) {
            for (String rId : input.getRestaurants()) {
                restaurantRepository.findById(rId).ifPresent(restaurants::add);
            }
        }
        SectionEntity section = SectionEntity.builder()
                .name(input.getName())
                .enabled(input.getEnabled() != null ? input.getEnabled() : true)
                .restaurants(restaurants)
                .build();
        return sectionMapper.toResponse(sectionRepository.save(section));
    }

    @Transactional
    public SectionResponse editSection(SectionInput input) {
        SecurityUtil.requireRole("ADMIN");
        SectionEntity section = sectionRepository.findById(input.getId())
                .orElseThrow(() -> new NotFoundException("Section", input.getId()));
        if (input.getName() != null) section.setName(input.getName());
        if (input.getEnabled() != null) section.setEnabled(input.getEnabled());
        if (input.getRestaurants() != null) {
            List<RestaurantEntity> restaurants = new ArrayList<>();
            for (String rId : input.getRestaurants()) {
                restaurantRepository.findById(rId).ifPresent(restaurants::add);
            }
            section.setRestaurants(restaurants);
        }
        return sectionMapper.toResponse(sectionRepository.save(section));
    }

    @Transactional
    public boolean deleteSection(String id) {
        SecurityUtil.requireRole("ADMIN");
        SectionEntity section = sectionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Section", id));
        sectionRepository.delete(section);
        return true;
    }
}
