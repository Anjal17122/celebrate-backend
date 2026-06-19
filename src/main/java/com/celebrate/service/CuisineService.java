package com.celebrate.service;

import com.celebrate.dto.input.CuisineFilterInput;
import com.celebrate.dto.input.CuisineInput;
import com.celebrate.dto.response.CuisineResponse;
import com.celebrate.entity.CuisineEntity;
import com.celebrate.exception.*;
import com.celebrate.mapper.CuisineMapper;
import com.celebrate.repository.CuisineRepository;
import com.celebrate.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CuisineService {

    private final CuisineRepository cuisineRepository;
    private final CuisineMapper cuisineMapper;

    public List<CuisineResponse> getAllCuisines() {
        return cuisineRepository.findAll().stream().map(cuisineMapper::toResponse).toList();
    }

    public Map<String, Object> getAllCuisinesFiltered(CuisineFilterInput input) {
        int pageNum = input != null && input.getPage() != null ? Math.max(0, input.getPage() - 1) : 0;
        int pageSize = input != null && input.getLimit() != null ? input.getLimit() : 10;
        String keyword = input != null ? input.getKeyword() : null;
        String shopType = input != null ? input.getShopType() : null;
        boolean paginate = input == null || !Boolean.FALSE.equals(input.getPaginate());

        List<CuisineEntity> all = cuisineRepository.findAll();
        List<CuisineEntity> filtered = all.stream()
                .filter(c -> keyword == null || c.getName().toLowerCase().contains(keyword.toLowerCase()))
                .filter(c -> shopType == null || shopType.equals(c.getShopType()))
                .toList();

        if (!paginate) {
            return Map.of(
                    "cuisines", filtered.stream().map(cuisineMapper::toResponse).toList(),
                    "docsCount", filtered.size(),
                    "totalPages", 1,
                    "currentPage", 1
            );
        }

        int total = filtered.size();
        int totalPages = (int) Math.ceil((double) total / pageSize);
        int start = pageNum * pageSize;
        int end = Math.min(start + pageSize, total);
        List<CuisineEntity> paginated = start < total ? filtered.subList(start, end) : List.of();

        return Map.of(
                "cuisines", paginated.stream().map(cuisineMapper::toResponse).toList(),
                "docsCount", total,
                "totalPages", totalPages,
                "currentPage", pageNum + 1
        );
    }

    public List<CuisineResponse> getNearByCuisines(Float latitude, Float longitude, String shopType) {
        return cuisineRepository.findAll().stream()
                .filter(c -> shopType == null || shopType.equals(c.getShopType()))
                .map(cuisineMapper::toResponse)
                .toList();
    }

    @Transactional
    public CuisineResponse createCuisine(CuisineInput input) {
        SecurityUtil.requireRole("ADMIN");
        CuisineEntity cuisine = CuisineEntity.builder()
                .name(input.getName())
                .description(input.getDescription())
                .image(input.getImage())
                .shopType(input.getShopType())
                .build();
        return cuisineMapper.toResponse(cuisineRepository.save(cuisine));
    }

    @Transactional
    public CuisineResponse editCuisine(CuisineInput input) {
        SecurityUtil.requireRole("ADMIN");
        CuisineEntity cuisine = cuisineRepository.findById(input.getId())
                .orElseThrow(() -> new NotFoundException("Cuisine", input.getId()));
        if (input.getName() != null) cuisine.setName(input.getName());
        if (input.getDescription() != null) cuisine.setDescription(input.getDescription());
        if (input.getImage() != null) cuisine.setImage(input.getImage());
        if (input.getShopType() != null) cuisine.setShopType(input.getShopType());
        return cuisineMapper.toResponse(cuisineRepository.save(cuisine));
    }

    @Transactional
    public String deleteCuisine(String id) {
        SecurityUtil.requireRole("ADMIN");
        CuisineEntity cuisine = cuisineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cuisine", id));
        cuisineRepository.delete(cuisine);
        return "Cuisine deleted.";
    }

    public CuisineResponse getCuisine(String cuisineName) {
        return cuisineRepository.findByName(cuisineName)
                .map(cuisineMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Cuisine with name: " + cuisineName));
    }
}
