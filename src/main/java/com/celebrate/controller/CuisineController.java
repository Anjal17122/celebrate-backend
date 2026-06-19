package com.celebrate.controller;

import com.celebrate.dto.input.CuisineFilterInput;
import com.celebrate.dto.input.CuisineInput;
import com.celebrate.dto.response.CuisineResponse;
import com.celebrate.service.CuisineService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CuisineController {

    private final CuisineService cuisineService;

    @QueryMapping
    public List<CuisineResponse> cuisines() {
        return cuisineService.getAllCuisines();
    }

    @QueryMapping("getAllCuisines")
    public Map<String, Object> getAllCuisines(@Argument("input") CuisineFilterInput input) {
        return cuisineService.getAllCuisinesFiltered(input);
    }

    @QueryMapping("nearByRestaurantsCuisines")
    public List<CuisineResponse> nearByRestaurantsCuisines(@Argument Float latitude, @Argument Float longitude, @Argument String shopType) {
        return cuisineService.getNearByCuisines(latitude, longitude, shopType);
    }

    @MutationMapping
    public CuisineResponse createCuisine(@Argument CuisineInput cuisineInput) {
        return cuisineService.createCuisine(cuisineInput);
    }

    @MutationMapping
    public CuisineResponse editCuisine(@Argument CuisineInput cuisineInput) {
        return cuisineService.editCuisine(cuisineInput);
    }

    @MutationMapping
    public String deleteCuisine(@Argument String id) {
        return cuisineService.deleteCuisine(id);
    }

    // Schema: cuisine(cuisine: String!): Cuisine! in Mutation block
    @MutationMapping
    public CuisineResponse cuisine(@Argument String cuisine) {
        return cuisineService.getCuisine(cuisine);
    }
}
