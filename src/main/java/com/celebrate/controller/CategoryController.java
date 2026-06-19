package com.celebrate.controller;

import com.celebrate.dto.input.*;
import com.celebrate.dto.response.*;
import com.celebrate.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @QueryMapping
    public List<CategoryResponse> categories() {
        return categoryService.getAllCategories();
    }

    @QueryMapping
    public List<SubCategoryResponse> subCategories() {
        return categoryService.getAllSubCategories();
    }

    @QueryMapping
    public List<SubCategoryResponse> subCategoriesByParentId(@Argument String parentCategoryId) {
        return categoryService.getSubCategoriesByParentId(parentCategoryId);
    }

    @QueryMapping
    public SubCategoryResponse subCategory(@Argument String _id) {
        return categoryService.getSubCategory(_id);
    }

    @QueryMapping
    public List<FoodResponse> foods() {
        return categoryService.getAllFoods();
    }

    @QueryMapping
    public List<FoodResponse> foodByCategory(@Argument String category, @Argument Boolean onSale,
            @Argument Boolean inStock, @Argument Float min, @Argument Float max, @Argument String search) {
        return categoryService.getFoodByCategory(category, onSale, inStock, min, max, search);
    }

    @QueryMapping
    public List<FoodResponse> likedFood() {
        return categoryService.getLikedFood();
    }

    @QueryMapping
    public List<OptionResponse> options() {
        return categoryService.getOptions();
    }

    @QueryMapping
    public List<AddonResponse> addons() {
        return categoryService.getAddons();
    }

    @MutationMapping
    public RestaurantResponse createCategory(@Argument CategoryInput category) {
        return categoryService.createCategory(category);
    }

    @MutationMapping
    public RestaurantResponse editCategory(@Argument CategoryInput category) {
        return categoryService.editCategory(category);
    }

    @MutationMapping
    public RestaurantResponse deleteCategory(@Argument String id, @Argument String restaurant) {
        return categoryService.deleteCategory(id, restaurant);
    }

    @MutationMapping
    public String createSubCategory(@Argument SubCategoryInput subCategory) {
        return categoryService.createSubCategory(subCategory);
    }

    @MutationMapping
    public String createSubCategories(@Argument List<SubCategoryInput> subCategories) {
        return categoryService.createSubCategories(subCategories);
    }

    @MutationMapping
    public SubCategoryResponse editSubCategory(@Argument SubCategoryInput subCategoryInput) {
        return categoryService.editSubCategory(subCategoryInput);
    }

    @MutationMapping
    public String deleteSubCategory(@Argument String _id) {
        return categoryService.deleteSubCategory(_id);
    }

    @MutationMapping
    public String deleteSubCategories(@Argument List<String> ids) {
        return categoryService.deleteSubCategories(ids);
    }

    @MutationMapping
    public RestaurantResponse createFood(@Argument FoodInput foodInput) {
        return categoryService.createFood(foodInput);
    }

    @MutationMapping
    public RestaurantResponse editFood(@Argument FoodInput foodInput) {
        return categoryService.editFood(foodInput);
    }

    @MutationMapping
    public RestaurantResponse deleteFood(@Argument String id, @Argument String restaurant, @Argument String categoryId) {
        return categoryService.deleteFood(id, restaurant, categoryId);
    }

    @MutationMapping
    public boolean updateFoodOutOfStock(@Argument String id, @Argument String restaurant, @Argument String categoryId) {
        return categoryService.updateFoodOutOfStock(id, restaurant, categoryId);
    }

    @MutationMapping
    public FoodResponse likeFood(@Argument String foodId) {
        return categoryService.likeFood(foodId);
    }

    @MutationMapping
    public FoodResponse toggleMenuFood(@Argument String id, @Argument String restaurant, @Argument String categoryId) {
        return categoryService.toggleMenuFood(id, restaurant, categoryId);
    }

    @MutationMapping
    public RestaurantResponse createOptions(@Argument CreateOptionInput optionInput) {
        return categoryService.createOptions(optionInput);
    }

    @MutationMapping
    public RestaurantResponse editOption(@Argument EditOptionInput optionInput) {
        return categoryService.editOption(optionInput);
    }

    @MutationMapping
    public RestaurantResponse deleteOption(@Argument String id, @Argument String restaurant) {
        return categoryService.deleteOption(id, restaurant);
    }

    @MutationMapping
    public RestaurantResponse createAddons(@Argument AddonInput addonInput) {
        return categoryService.createAddons(addonInput);
    }

    @MutationMapping
    public RestaurantResponse editAddon(@Argument EditAddonInput addonInput) {
        return categoryService.editAddon(addonInput);
    }

    @MutationMapping
    public RestaurantResponse deleteAddon(@Argument String id, @Argument String restaurant) {
        return categoryService.deleteAddon(id, restaurant);
    }

    @QueryMapping
    public List<CartFoodResponse> foodByIds(@Argument("foodIds") List<CartFoodInput> foodIds) {
        return categoryService.foodByIds(foodIds);
    }
}
