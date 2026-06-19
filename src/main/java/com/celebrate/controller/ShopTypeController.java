package com.celebrate.controller;

import com.celebrate.dto.input.*;
import com.celebrate.dto.response.ShopTypeResponse;
import com.celebrate.service.ShopTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ShopTypeController {

    private final ShopTypeService shopTypeService;

    @QueryMapping("fetchShopTypes")
    public Map<String, Object> fetchShopTypes(@Argument FetchShopTypeFilter filter, @Argument PaginationInput pagination) {
        return shopTypeService.fetchShopTypes(filter, pagination);
    }

    @QueryMapping("fetchAllShopTypes")
    public Map<String, Object> fetchAllShopTypes() {
        return shopTypeService.fetchAllShopTypes();
    }

    @QueryMapping("fetchShopTypeByUnique")
    public ShopTypeResponse fetchShopTypeByUnique(@Argument("dto") FetchUniqueShopTypeInput dto) {
        return shopTypeService.fetchShopTypeByUnique(dto);
    }

    @MutationMapping
    public ShopTypeResponse createShopType(@Argument("dto") CreateShopTypeInput dto) {
        return shopTypeService.createShopType(dto);
    }

    @MutationMapping
    public ShopTypeResponse updateShopType(@Argument("dto") UpdateShopTypeInput dto) {
        return shopTypeService.updateShopType(dto);
    }

    @MutationMapping
    public ShopTypeResponse deleteShopType(@Argument String id, @Argument String type) {
        return shopTypeService.deleteShopType(id, type);
    }
}
