package com.celebrate.service;

import com.celebrate.dto.input.*;
import com.celebrate.dto.response.*;
import com.celebrate.entity.*;
import com.celebrate.exception.*;
import com.celebrate.mapper.*;
import com.celebrate.repository.*;
import com.celebrate.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final FoodRepository foodRepository;
    private final VariationRepository variationRepository;
    private final OptionRepository optionRepository;
    private final AddonRepository addonRepository;
    private final RestaurantMapper restaurantMapper;
    private final FoodMapper foodMapper;

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(c -> CategoryResponse.builder()
                        .id(c.getId())
                        .title(c.getTitle())
                        .image(c.getImage())
                        .build())
                .toList();
    }

    public List<SubCategoryResponse> getAllSubCategories() {
        return subCategoryRepository.findAll().stream()
                .map(s -> SubCategoryResponse.builder()
                        .id(s.getId())
                        .title(s.getTitle())
                        .parentCategoryId(s.getParentCategory().getId())
                        .build())
                .toList();
    }

    public List<SubCategoryResponse> getSubCategoriesByParentId(String parentCategoryId) {
        return subCategoryRepository.findByParentCategoryId(parentCategoryId).stream()
                .map(s -> SubCategoryResponse.builder()
                        .id(s.getId())
                        .title(s.getTitle())
                        .parentCategoryId(parentCategoryId)
                        .build())
                .toList();
    }

    public SubCategoryResponse getSubCategory(String id) {
        SubCategoryEntity sub = subCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("SubCategory", id));
        return SubCategoryResponse.builder()
                .id(sub.getId())
                .title(sub.getTitle())
                .parentCategoryId(sub.getParentCategory().getId())
                .build();
    }

    @Transactional
    public RestaurantResponse createCategory(CategoryInput input) {
        RestaurantEntity restaurant = restaurantRepository.findById(input.getRestaurant())
                .orElseThrow(() -> new NotFoundException("Restaurant", input.getRestaurant()));

        CategoryEntity category = CategoryEntity.builder()
                .title(input.getTitle())
                .image(input.getImage())
                .restaurant(restaurant)
                .build();

        categoryRepository.save(category);

        if (input.getSubCategories() != null) {
            for (SubCategoryInput subInput : input.getSubCategories()) {
                if (subInput != null) {
                    SubCategoryEntity sub = SubCategoryEntity.builder()
                            .title(subInput.getTitle())
                            .parentCategory(category)
                            .build();
                    subCategoryRepository.save(sub);
                }
            }
        }

        return restaurantMapper.toResponse(restaurantRepository.findById(restaurant.getId()).orElseThrow());
    }

    @Transactional
    public RestaurantResponse editCategory(CategoryInput input) {
        CategoryEntity category = categoryRepository.findById(input.getId())
                .orElseThrow(() -> new NotFoundException("Category", input.getId()));
        RestaurantEntity restaurant = restaurantRepository.findById(input.getRestaurant())
                .orElseThrow(() -> new NotFoundException("Restaurant", input.getRestaurant()));

        category.setTitle(input.getTitle());
        if (input.getImage() != null) category.setImage(input.getImage());
        categoryRepository.save(category);

        return restaurantMapper.toResponse(restaurantRepository.findById(restaurant.getId()).orElseThrow());
    }

    @Transactional
    public RestaurantResponse deleteCategory(String id, String restaurantId) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category", id));
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant", restaurantId));

        categoryRepository.delete(category);
        return restaurantMapper.toResponse(restaurantRepository.findById(restaurantId).orElseThrow());
    }

    @Transactional
    public String createSubCategory(SubCategoryInput input) {
        CategoryEntity parent = categoryRepository.findById(input.getParentCategoryId())
                .orElseThrow(() -> new NotFoundException("Category", input.getParentCategoryId()));
        SubCategoryEntity sub = SubCategoryEntity.builder()
                .title(input.getTitle())
                .parentCategory(parent)
                .build();
        return subCategoryRepository.save(sub).getId();
    }

    @Transactional
    public String createSubCategories(List<SubCategoryInput> inputs) {
        for (SubCategoryInput input : inputs) {
            if (input != null) createSubCategory(input);
        }
        return "SubCategories created successfully.";
    }

    @Transactional
    public SubCategoryResponse editSubCategory(SubCategoryInput input) {
        SubCategoryEntity sub = subCategoryRepository.findById(input.getId())
                .orElseThrow(() -> new NotFoundException("SubCategory", input.getId()));
        sub.setTitle(input.getTitle());
        SubCategoryEntity saved = subCategoryRepository.save(sub);
        return SubCategoryResponse.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .parentCategoryId(saved.getParentCategory().getId())
                .build();
    }

    @Transactional
    public String deleteSubCategory(String id) {
        SubCategoryEntity sub = subCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("SubCategory", id));
        subCategoryRepository.delete(sub);
        return "Deleted successfully.";
    }

    @Transactional
    public String deleteSubCategories(List<String> ids) {
        subCategoryRepository.deleteAllById(ids);
        return "Deleted successfully.";
    }

    // Food management
    public List<FoodResponse> getAllFoods() {
        return foodRepository.findAll().stream().map(foodMapper::toResponse).toList();
    }

    public List<FoodResponse> getFoodByCategory(String categoryId, Boolean onSale, Boolean inStock, Float min, Float max, String search) {
        return foodRepository.findByCategoryId(categoryId).stream()
                .filter(f -> inStock == null || !inStock || !Boolean.TRUE.equals(f.getIsOutOfStock()))
                .filter(f -> search == null || f.getTitle().toLowerCase().contains(search.toLowerCase()))
                .map(foodMapper::toResponse)
                .toList();
    }

    public List<FoodResponse> getLikedFood() {
        String userId = SecurityUtil.getCurrentUserId();
        return foodRepository.findLikedByUserId(userId).stream().map(foodMapper::toResponse).toList();
    }

    @Transactional
    public RestaurantResponse createFood(FoodInput input) {
        RestaurantEntity restaurant = restaurantRepository.findById(input.getRestaurant())
                .orElseThrow(() -> new NotFoundException("Restaurant", input.getRestaurant()));
        CategoryEntity category = categoryRepository.findById(input.getCategory())
                .orElseThrow(() -> new NotFoundException("Category", input.getCategory()));

        SubCategoryEntity subCategory = null;
        if (input.getSubCategory() != null) {
            subCategory = subCategoryRepository.findById(input.getSubCategory()).orElse(null);
        }

        FoodEntity food = FoodEntity.builder()
                .title(input.getTitle())
                .description(input.getDescription())
                .image(input.getImage())
                .category(category)
                .subCategory(subCategory)
                .isActive(input.getIsActive() != null ? input.getIsActive() : true)
                .isOutOfStock(input.getIsOutOfStock() != null ? input.getIsOutOfStock() : false)
                .build();

        FoodEntity savedFood = foodRepository.save(food);

        if (input.getVariations() != null) {
            for (VariationInput vi : input.getVariations()) {
                VariationEntity variation = VariationEntity.builder()
                        .title(vi.getTitle())
                        .price(vi.getPrice().doubleValue())
                        .discounted(vi.getDiscounted() != null ? vi.getDiscounted().doubleValue() : 0.0)
                        .isOutOfStock(vi.getIsOutOfStock() != null ? vi.getIsOutOfStock() : false)
                        .food(savedFood)
                        .addonIds(vi.getAddons())
                        .build();
                variationRepository.save(variation);
            }
        }

        return restaurantMapper.toResponse(restaurantRepository.findById(restaurant.getId()).orElseThrow());
    }

    @Transactional
    public RestaurantResponse editFood(FoodInput input) {
        FoodEntity food = foodRepository.findById(input.getId())
                .orElseThrow(() -> new NotFoundException("Food", input.getId()));
        RestaurantEntity restaurant = restaurantRepository.findById(input.getRestaurant())
                .orElseThrow(() -> new NotFoundException("Restaurant", input.getRestaurant()));

        if (input.getTitle() != null) food.setTitle(input.getTitle());
        if (input.getDescription() != null) food.setDescription(input.getDescription());
        if (input.getImage() != null) food.setImage(input.getImage());
        if (input.getIsActive() != null) food.setIsActive(input.getIsActive());
        if (input.getIsOutOfStock() != null) food.setIsOutOfStock(input.getIsOutOfStock());

        if (input.getCategory() != null) {
            CategoryEntity cat = categoryRepository.findById(input.getCategory()).orElseThrow();
            food.setCategory(cat);
        }

        if (input.getVariations() != null) {
            variationRepository.deleteByFoodId(food.getId());
            for (VariationInput vi : input.getVariations()) {
                VariationEntity variation = VariationEntity.builder()
                        .title(vi.getTitle())
                        .price(vi.getPrice().doubleValue())
                        .discounted(vi.getDiscounted() != null ? vi.getDiscounted().doubleValue() : 0.0)
                        .isOutOfStock(vi.getIsOutOfStock() != null ? vi.getIsOutOfStock() : false)
                        .food(food)
                        .addonIds(vi.getAddons())
                        .build();
                variationRepository.save(variation);
            }
        }

        foodRepository.save(food);
        return restaurantMapper.toResponse(restaurantRepository.findById(restaurant.getId()).orElseThrow());
    }

    @Transactional
    public RestaurantResponse deleteFood(String id, String restaurantId, String categoryId) {
        FoodEntity food = foodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Food", id));
        foodRepository.delete(food);
        return restaurantMapper.toResponse(restaurantRepository.findById(restaurantId).orElseThrow());
    }

    @Transactional
    public boolean updateFoodOutOfStock(String id, String restaurantId, String categoryId) {
        FoodEntity food = foodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Food", id));
        food.setIsOutOfStock(!Boolean.TRUE.equals(food.getIsOutOfStock()));
        foodRepository.save(food);
        return true;
    }

    @Transactional
    public FoodResponse likeFood(String foodId) {
        FoodEntity food = foodRepository.findById(foodId)
                .orElseThrow(() -> new NotFoundException("Food", foodId));
        // Toggle logic can be done with a user_likes table; here we just return the food
        return foodMapper.toResponse(food);
    }

    @Transactional
    public FoodResponse toggleMenuFood(String id, String restaurantId, String categoryId) {
        FoodEntity food = foodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Food", id));
        food.setIsActive(!Boolean.TRUE.equals(food.getIsActive()));
        return foodMapper.toResponse(foodRepository.save(food));
    }

    // Options
    public List<OptionResponse> getOptions() {
        return optionRepository.findAll().stream()
                .map(o -> OptionResponse.builder()
                        .id(o.getId()).title(o.getTitle())
                        .description(o.getDescription())
                        .price(o.getPrice())
                        .isOutOfStock(o.getIsOutOfStock())
                        .build())
                .toList();
    }

    @Transactional
    public RestaurantResponse createOptions(CreateOptionInput input) {
        RestaurantEntity restaurant = restaurantRepository.findById(input.getRestaurant())
                .orElseThrow(() -> new NotFoundException("Restaurant", input.getRestaurant()));

        for (OptionInput oi : input.getOptions()) {
            OptionEntity option = OptionEntity.builder()
                    .title(oi.getTitle())
                    .description(oi.getDescription())
                    .price(oi.getPrice().doubleValue())
                    .restaurant(restaurant)
                    .build();
            optionRepository.save(option);
        }
        return restaurantMapper.toResponse(restaurantRepository.findById(restaurant.getId()).orElseThrow());
    }

    @Transactional
    public RestaurantResponse editOption(EditOptionInput input) {
        RestaurantEntity restaurant = restaurantRepository.findById(input.getRestaurant())
                .orElseThrow(() -> new NotFoundException("Restaurant", input.getRestaurant()));
        OptionInput oi = input.getOptions();
        OptionEntity option = optionRepository.findById(oi.getId())
                .orElseThrow(() -> new NotFoundException("Option", oi.getId()));
        if (oi.getTitle() != null) option.setTitle(oi.getTitle());
        if (oi.getDescription() != null) option.setDescription(oi.getDescription());
        if (oi.getPrice() != null) option.setPrice(oi.getPrice().doubleValue());
        optionRepository.save(option);
        return restaurantMapper.toResponse(restaurantRepository.findById(restaurant.getId()).orElseThrow());
    }

    @Transactional
    public RestaurantResponse deleteOption(String id, String restaurantId) {
        OptionEntity option = optionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Option", id));
        optionRepository.delete(option);
        return restaurantMapper.toResponse(restaurantRepository.findById(restaurantId).orElseThrow());
    }

    // Addons
    public List<AddonResponse> getAddons() {
        return addonRepository.findAll().stream()
                .map(a -> AddonResponse.builder()
                        .id(a.getId()).title(a.getTitle())
                        .description(a.getDescription())
                        .quantityMinimum(a.getQuantityMinimum())
                        .quantityMaximum(a.getQuantityMaximum())
                        .options(a.getOptionIds())
                        .isOutOfStock(a.getIsOutOfStock())
                        .build())
                .toList();
    }

    @Transactional
    public RestaurantResponse createAddons(AddonInput input) {
        RestaurantEntity restaurant = restaurantRepository.findById(input.getRestaurant())
                .orElseThrow(() -> new NotFoundException("Restaurant", input.getRestaurant()));

        for (CreateAddonInput ai : input.getAddons()) {
            AddonEntity addon = AddonEntity.builder()
                    .title(ai.getTitle())
                    .description(ai.getDescription())
                    .quantityMinimum(ai.getQuantityMinimum())
                    .quantityMaximum(ai.getQuantityMaximum())
                    .optionIds(ai.getOptions())
                    .restaurant(restaurant)
                    .build();
            addonRepository.save(addon);
        }
        return restaurantMapper.toResponse(restaurantRepository.findById(restaurant.getId()).orElseThrow());
    }

    @Transactional
    public RestaurantResponse editAddon(EditAddonInput input) {
        RestaurantEntity restaurant = restaurantRepository.findById(input.getRestaurant())
                .orElseThrow(() -> new NotFoundException("Restaurant", input.getRestaurant()));
        CreateAddonInput ai = input.getAddons();
        AddonEntity addon = addonRepository.findById(ai.getId())
                .orElseThrow(() -> new NotFoundException("Addon", ai.getId()));
        if (ai.getTitle() != null) addon.setTitle(ai.getTitle());
        if (ai.getDescription() != null) addon.setDescription(ai.getDescription());
        if (ai.getQuantityMinimum() != null) addon.setQuantityMinimum(ai.getQuantityMinimum());
        if (ai.getQuantityMaximum() != null) addon.setQuantityMaximum(ai.getQuantityMaximum());
        if (ai.getOptions() != null) addon.setOptionIds(ai.getOptions());
        addonRepository.save(addon);
        return restaurantMapper.toResponse(restaurantRepository.findById(restaurant.getId()).orElseThrow());
    }

    @Transactional
    public RestaurantResponse deleteAddon(String id, String restaurantId) {
        AddonEntity addon = addonRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Addon", id));
        addonRepository.delete(addon);
        return restaurantMapper.toResponse(restaurantRepository.findById(restaurantId).orElseThrow());
    }

    public List<CartFoodResponse> foodByIds(List<CartFoodInput> foodIds) {
        if (foodIds == null || foodIds.isEmpty()) return List.of();

        List<String> ids = foodIds.stream()
                .filter(f -> f.get_id() != null)
                .map(CartFoodInput::get_id)
                .toList();

        Map<String, FoodEntity> foodMap = foodRepository.findByIdIn(ids).stream()
                .collect(Collectors.toMap(FoodEntity::getId, f -> f));

        return foodIds.stream().map(input -> {
            FoodEntity food = foodMap.get(input.get_id());
            if (food == null) return null;

            String variationId = input.getVariation() != null ? input.getVariation().getId() : null;
            VariationEntity variation = variationId != null
                    ? variationRepository.findById(variationId).orElse(null)
                    : null;

            CartVariationResponse varResponse = null;
            if (variation != null) {
                List<CartAddonResponse> addonResponses = List.of();
                if (variation.getAddonIds() != null && !variation.getAddonIds().isEmpty()) {
                    addonResponses = variation.getAddonIds().stream().map(addonId -> {
                        AddonEntity a = addonRepository.findById(addonId).orElse(null);
                        if (a == null) return null;
                        List<OptionResponse> optionResponses = a.getOptionIds() != null
                                ? a.getOptionIds().stream().map(optId ->
                                        optionRepository.findById(optId).map(o ->
                                                OptionResponse.builder()
                                                        .id(o.getId())
                                                        .title(o.getTitle())
                                                        .description(o.getDescription())
                                                        .price(o.getPrice())
                                                        .build()
                                        ).orElse(null)
                                ).filter(o -> o != null).toList()
                                : List.of();
                        return CartAddonResponse.builder()
                                .id(a.getId())
                                .title(a.getTitle())
                                .description(a.getDescription())
                                .quantityMinimum(a.getQuantityMinimum())
                                .quantityMaximum(a.getQuantityMaximum())
                                .options(optionResponses)
                                .build();
                    }).filter(a -> a != null).toList();
                }
                varResponse = CartVariationResponse.builder()
                        .id(variation.getId())
                        .title(variation.getTitle())
                        .price(variation.getPrice())
                        .discounted(variation.getDiscounted())
                        .addons(addonResponses)
                        .build();
            }

            return CartFoodResponse.builder()
                    .id(food.getId())
                    .title(food.getTitle())
                    .description(food.getDescription())
                    .image(food.getImage())
                    .isActive(food.getIsActive())
                    .variation(varResponse)
                    .createdAt(food.getCreatedAt() != null ? food.getCreatedAt().toString() : "")
                    .updatedAt(food.getUpdatedAt() != null ? food.getUpdatedAt().toString() : "")
                    .build();
        }).filter(f -> f != null).toList();
    }
}
