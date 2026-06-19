package com.celebrate.service;

import com.celebrate.dto.input.*;
import com.celebrate.dto.response.ShopTypeResponse;
import com.celebrate.entity.ShopTypeEntity;
import com.celebrate.exception.*;
import com.celebrate.mapper.ShopTypeMapper;
import com.celebrate.repository.ShopTypeRepository;
import com.celebrate.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ShopTypeService {

    private final ShopTypeRepository shopTypeRepository;
    private final ShopTypeMapper shopTypeMapper;

    public ShopTypeResponse fetchShopTypeByUnique(FetchUniqueShopTypeInput dto) {
        if (dto == null) return null;
        if (dto.getId() != null) {
            return shopTypeMapper.toResponse(shopTypeRepository.findById(dto.getId())
                    .orElseThrow(() -> new NotFoundException("ShopType", dto.getId())));
        }
        if (dto.getName() != null) {
            return shopTypeMapper.toResponse(shopTypeRepository.findByName(dto.getName())
                    .orElseThrow(() -> new NotFoundException("ShopType with name: " + dto.getName())));
        }
        return null;
    }

    public Map<String, Object> fetchShopTypes(FetchShopTypeFilter filter, PaginationInput pagination) {
        int pageNum = pagination != null && pagination.getPageNo() != null ? Math.max(0, pagination.getPageNo() - 1) : 0;
        int pageSize = pagination != null && pagination.getPageSize() != null ? pagination.getPageSize() : 10;
        String search = filter != null ? filter.getSearch() : null;
        Boolean isDeleted = filter != null ? filter.getIsDeleted() : null;

        Page<ShopTypeEntity> result;
        if (Boolean.TRUE.equals(isDeleted)) {
            result = shopTypeRepository.findDeletedWithSearch(search, PageRequest.of(pageNum, pageSize));
        } else {
            result = shopTypeRepository.findActiveWithSearch(search, PageRequest.of(pageNum, pageSize));
        }

        return Map.of(
                "data", result.getContent().stream().map(shopTypeMapper::toResponse).toList(),
                "total", result.getTotalElements(),
                "page", pageNum + 1,
                "pageSize", pageSize,
                "totalPages", result.getTotalPages(),
                "hasNextPage", result.hasNext(),
                "hasPrevPage", result.hasPrevious()
        );
    }

    public Map<String, Object> fetchAllShopTypes() {
        List<ShopTypeResponse> data = shopTypeRepository.findAll().stream()
                .filter(s -> s.getDeletedAt() == null)
                .map(shopTypeMapper::toResponse)
                .toList();
        return Map.of("data", data);
    }

    @Transactional
    public ShopTypeResponse createShopType(CreateShopTypeInput dto) {
        SecurityUtil.requireRole("ADMIN");
        ShopTypeEntity shopType = ShopTypeEntity.builder()
                .name(dto.getName())
                .image(dto.getImage())
                .slug(dto.getName().toLowerCase().replaceAll("[^a-z0-9]+", "-"))
                .isActive(true)
                .build();
        return shopTypeMapper.toResponse(shopTypeRepository.save(shopType));
    }

    @Transactional
    public ShopTypeResponse updateShopType(UpdateShopTypeInput dto) {
        SecurityUtil.requireRole("ADMIN");
        ShopTypeEntity shopType = shopTypeRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException("ShopType", dto.getId()));
        if (dto.getName() != null) shopType.setName(dto.getName());
        if (dto.getImage() != null) shopType.setImage(dto.getImage());
        if (dto.getIsActive() != null) shopType.setIsActive(dto.getIsActive());
        return shopTypeMapper.toResponse(shopTypeRepository.save(shopType));
    }

    @Transactional
    public ShopTypeResponse deleteShopType(String id, String type) {
        SecurityUtil.requireRole("ADMIN");
        ShopTypeEntity shopType = shopTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ShopType", id));
        if ("HARD".equals(type)) {
            shopTypeRepository.delete(shopType);
        } else {
            shopType.setDeletedAt(LocalDateTime.now());
            shopTypeRepository.save(shopType);
        }
        return shopTypeMapper.toResponse(shopType);
    }
}
