package com.celebrate.service;

import com.celebrate.dto.input.*;
import com.celebrate.dto.response.*;
import com.celebrate.entity.*;
import com.celebrate.exception.*;
import com.celebrate.mapper.RestaurantMapper;
import com.celebrate.repository.*;
import com.celebrate.security.SecurityUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final OwnerRepository ownerRepository;
    private final ZoneRepository zoneRepository;
    private final ReviewRepository reviewRepository;
    private final RestaurantMapper restaurantMapper;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final CategoryRepository categoryRepository;
    private final OfferRepository offerRepository;
    private final SectionRepository sectionRepository;
    private final FoodRepository foodRepository;
    private final OrderRepository orderRepository;
    private final ShopTypeRepository shopTypeRepository;

    public RestaurantResponse getRestaurant(String id, String slug) {
        RestaurantEntity restaurant;
        if (id != null) {
            restaurant = restaurantRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Restaurant", id));
        } else if (slug != null) {
            restaurant = restaurantRepository.findBySlug(slug)
                    .orElseThrow(() -> new NotFoundException("No restaurant with slug: " + slug));
        } else {
            throw new BadRequestException("Either id or slug must be provided.");
        }
        return restaurantMapper.toResponse(restaurant);
    }

    public List<RestaurantResponse> getRestaurants(Integer page, Integer limit) {
        int pageNum = page != null ? Math.max(0, page - 1) : 0;
        int pageSize = limit != null ? limit : 10;
        return restaurantRepository.findAll(PageRequest.of(pageNum, pageSize))
                .stream().map(restaurantMapper::toResponse).toList();
    }

    public List<RestaurantResponse> getRestaurantList() {
        return restaurantRepository.findAll().stream().map(restaurantMapper::toResponse).toList();
    }

    public Map<String, Object> getRestaurantsPaginated(Integer page, Integer limit, String search) {
        int pageNum = page != null ? Math.max(0, page - 1) : 0;
        int pageSize = limit != null ? limit : 10;
        Page<RestaurantEntity> result = restaurantRepository.findAllWithSearch(search, PageRequest.of(pageNum, pageSize));
        return Map.of(
                "data", result.getContent().stream().map(restaurantMapper::toResponse).toList(),
                "totalCount", result.getTotalElements(),
                "currentPage", pageNum + 1,
                "totalPages", result.getTotalPages()
        );
    }

    public Map<String, Object> getClonedRestaurantsPaginated(Integer page, Integer limit, String search) {
        SecurityUtil.requireRole("ADMIN");
        int pageNum = page != null ? Math.max(0, page - 1) : 0;
        int pageSize = limit != null ? limit : 10;
        Page<RestaurantEntity> result = restaurantRepository.findClonedWithSearch(search, PageRequest.of(pageNum, pageSize));
        return Map.of(
                "data", result.getContent().stream().map(restaurantMapper::toResponse).toList(),
                "totalCount", result.getTotalElements(),
                "currentPage", pageNum + 1,
                "totalPages", result.getTotalPages()
        );
    }

    public List<RestaurantResponse> getClonedRestaurants() {
        SecurityUtil.requireRole("ADMIN");
        return restaurantRepository.findByIsClonedTrue().stream().map(restaurantMapper::toResponse).toList();
    }

    public Map<String, Object> getCommissionRate(Integer page, Integer limit) {
        SecurityUtil.requireRole("ADMIN");
        int pageNum = page != null ? Math.max(0, page - 1) : 0;
        int pageSize = limit != null ? limit : 10;
        Page<RestaurantEntity> result = restaurantRepository.findAll(PageRequest.of(pageNum, pageSize));
        List<Map<String, Object>> commissions = result.getContent().stream().map(r ->
                Map.<String, Object>of(
                        "id", r.getId(),
                        "unique_restaurant_id", r.getUniqueRestaurantId() != null ? r.getUniqueRestaurantId() : "",
                        "orderId", r.getOrderId() != null ? r.getOrderId() : 0,
                        "orderPrefix", r.getOrderPrefix() != null ? r.getOrderPrefix() : "",
                        "name", r.getName(),
                        "commissionRate", r.getCommissionRate() != null ? r.getCommissionRate() : 0.0
                )).toList();
        return Map.of(
                "restaurant", commissions,
                "currentPage", pageNum + 1,
                "totalPages", result.getTotalPages(),
                "nextPage", result.hasNext(),
                "prevPage", result.hasPrevious()
        );
    }

    public int getPageCount(String restaurantId) {
        return restaurantRepository.countByOrderedItems(restaurantId);
    }

    @Transactional
    public RestaurantResponse createRestaurant(RestaurantInput input, String ownerId) {
        SecurityUtil.requireRole("ADMIN");
        OwnerEntity owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Owner", ownerId));

        String slug = generateSlug(input.getName());
        ShopTypeEntity shopType = shopTypeRepository.findById(input.getShopType()).orElse(null);
        RestaurantEntity restaurant = RestaurantEntity.builder()
                .name(input.getName())
                .username(input.getUsername())
                .password(input.getPassword() != null ? passwordEncoder.encode(input.getPassword()) : null)
                .image(input.getImage())
                .logo(input.getLogo())
                .address(input.getAddress())
                .deliveryTime(input.getDeliveryTime())
                .minimumOrder(input.getMinimumOrder())
                .tax(input.getSalesTax() != null ? input.getSalesTax().doubleValue() : null)
                .shopType(shopType)
                .cuisines(input.getCuisines())
                .restaurantUrl(input.getRestaurantUrl())
                .phone(input.getPhone())
                .slug(slug)
                .isActive(true)
                .isAvailable(true)
                .isCloned(false)
                .stripeDetailsSubmitted(false)
                .currentWalletAmount(0.0)
                .totalWalletAmount(0.0)
                .withdrawnWalletAmount(0.0)
                .owner(owner)
                .build();

        applyBussinessDetails(restaurant, input.getBussinessDetails());
        return restaurantMapper.toResponse(restaurantRepository.save(restaurant));
    }

    @Transactional
    public RestaurantResponse editRestaurant(RestaurantProfileInput input) {
        String restaurantId = input.getId();
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant", restaurantId));

        if (input.getName() != null) restaurant.setName(input.getName());
        if (input.getImage() != null) restaurant.setImage(input.getImage());
        if (input.getLogo() != null) restaurant.setLogo(input.getLogo());
        if (input.getAddress() != null) restaurant.setAddress(input.getAddress());
        if (input.getOrderPrefix() != null) restaurant.setOrderPrefix(input.getOrderPrefix());
        if (input.getUsername() != null) restaurant.setUsername(input.getUsername());
        if (input.getPassword() != null) restaurant.setPassword(passwordEncoder.encode(input.getPassword()));
        if (input.getDeliveryTime() != null) restaurant.setDeliveryTime(input.getDeliveryTime());
        if (input.getMinimumOrder() != null) restaurant.setMinimumOrder(input.getMinimumOrder());
        if (input.getSalesTax() != null) restaurant.setTax(input.getSalesTax().doubleValue());
        if (input.getShopType() != null) restaurant.setShopType(shopTypeRepository.findById(input.getShopType()).orElse(null));
        if (input.getCuisines() != null) restaurant.setCuisines(input.getCuisines());
        if (input.getRestaurantUrl() != null) restaurant.setRestaurantUrl(input.getRestaurantUrl());
        if (input.getPhone() != null) restaurant.setPhone(input.getPhone());
        if (input.getBussinessDetails() != null) applyBussinessDetails(restaurant, input.getBussinessDetails());

        if (input.getOwner() != null) {
            OwnerEntity owner = ownerRepository.findById(input.getOwner())
                    .orElseThrow(() -> new NotFoundException("Owner", input.getOwner()));
            restaurant.setOwner(owner);
        }

        return restaurantMapper.toResponse(restaurantRepository.save(restaurant));
    }

    @Transactional
    public RestaurantResponse deleteRestaurant(String id) {
        SecurityUtil.requireRole("ADMIN");
        RestaurantEntity restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant", id));
        restaurant.setIsActive(false);
        return restaurantMapper.toResponse(restaurantRepository.save(restaurant));
    }

    @Transactional
    public boolean hardDeleteRestaurant(String id) {
        SecurityUtil.requireRole("ADMIN");
        RestaurantEntity restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant", id));
        restaurantRepository.delete(restaurant);
        return true;
    }

    @Transactional
    public RestaurantResponse duplicateRestaurant(String id, String ownerId) {
        SecurityUtil.requireRole("ADMIN");
        RestaurantEntity original = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant", id));
        OwnerEntity owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Owner", ownerId));

        RestaurantEntity clone = RestaurantEntity.builder()
                .name(original.getName() + " (Copy)")
                .image(original.getImage())
                .logo(original.getLogo())
                .address(original.getAddress())
                .deliveryTime(original.getDeliveryTime())
                .minimumOrder(original.getMinimumOrder())
                .tax(original.getTax())
                .shopType(original.getShopType())
                .cuisines(original.getCuisines() != null ? new ArrayList<>(original.getCuisines()) : null)
                .slug(generateSlug(original.getName() + "-copy-" + UUID.randomUUID().toString().substring(0, 4)))
                .isActive(false)
                .isAvailable(false)
                .isCloned(true)
                .stripeDetailsSubmitted(false)
                .currentWalletAmount(0.0)
                .totalWalletAmount(0.0)
                .withdrawnWalletAmount(0.0)
                .owner(owner)
                .build();

        return restaurantMapper.toResponse(restaurantRepository.save(clone));
    }

    @Transactional
    public RestaurantResponse toggleAvailability(String restaurantId) {
        String id = restaurantId != null ? restaurantId : SecurityUtil.getCurrentUserId();
        RestaurantEntity restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant", id));
        restaurant.setIsAvailable(!Boolean.TRUE.equals(restaurant.getIsAvailable()));
        return restaurantMapper.toResponse(restaurantRepository.save(restaurant));
    }

    @Transactional
    public RestaurantResponse toggleStoreAvailability(String restaurantId) {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant", restaurantId));
        restaurant.setIsAvailable(!Boolean.TRUE.equals(restaurant.getIsAvailable()));
        return restaurantMapper.toResponse(restaurantRepository.save(restaurant));
    }

    @Transactional
    public RestaurantResponse updateTimings(String id, List<TimingsInput> openingTimes) {
        RestaurantEntity restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant", id));

        // Remove existing timings and re-create them
        if (restaurant.getOpeningTimes() != null) {
            restaurant.getOpeningTimes().clear();
        }else{
            restaurant.setOpeningTimes(new ArrayList<>());
        }

        if (openingTimes != null) {
            List<OpeningTimeEntity> entities = openingTimes.stream().map(t -> {
                OpeningTimeEntity ote = new OpeningTimeEntity();
                ote.setDay(t.getDay());
                ote.setRestaurant(restaurant);
                if (t.getTimes() != null) {
                    List<TimingEntity> timings = t.getTimes().stream().map(ti -> {
                        TimingEntity timing = new TimingEntity();
                        timing.setStartTime(ti.getStartTime() != null ? String.join(",", ti.getStartTime()) : null);
                        timing.setEndTime(ti.getEndTime() != null ? String.join(",", ti.getEndTime()) : null);
                        timing.setOpeningTime(ote);
                        return timing;
                    }).toList();
                    ote.setTimes(timings);
                }
                return ote;
            }).toList();
            restaurant.getOpeningTimes().addAll(entities);
//            restaurant.setOpeningTimes(entities);
        }

        return restaurantMapper.toResponse(restaurantRepository.save(restaurant));
    }

    @Transactional
    public RestaurantResponse updateCommission(String id, Float commissionRate) {
        SecurityUtil.requireRole("ADMIN");
        RestaurantEntity restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant", id));
        restaurant.setCommissionRate(commissionRate.doubleValue());
        return restaurantMapper.toResponse(restaurantRepository.save(restaurant));
    }

    @Transactional
    public RestaurantOperationResponse updateDeliveryBoundsAndLocation(
            String id, String boundType, List<List<List<Float>>> bounds,
            CircleBoundsInput circleBounds, CoordinatesInput location,
            String address, String postCode, String city) {

        RestaurantEntity restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant", id));

        restaurant.setDeliveryBoundsType(boundType);
        restaurant.setLat(String.valueOf(location.getLatitude()));
        restaurant.setLng(String.valueOf(location.getLongitude()));
        if (address != null) restaurant.setAddress(address);
        if (postCode != null) restaurant.setPostCode(postCode);
        if (city != null) restaurant.setCity(city);

        if (bounds != null) {
            try {
                restaurant.setDeliveryBounds(objectMapper.writeValueAsString(bounds));
            } catch (JsonProcessingException e) {
                throw new BadRequestException("Invalid delivery bounds format.");
            }
        }
        if (circleBounds != null && circleBounds.getRadius() != null) {
            restaurant.setCircleBoundsRadius(circleBounds.getRadius().doubleValue());
        }

        return new RestaurantOperationResponse(true, null, restaurantMapper.toResponse(restaurantRepository.save(restaurant)));
    }

    @Transactional
    public RestaurantOperationResponse updateRestaurantDelivery(String id, Float minDeliveryFee, Float deliveryDistance, Float deliveryFee) {
        RestaurantEntity restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant", id));
        if (minDeliveryFee != null) restaurant.setMinDeliveryFee(minDeliveryFee.doubleValue());
        if (deliveryDistance != null) restaurant.setDeliveryDistance(deliveryDistance.doubleValue());
        if (deliveryFee != null) restaurant.setDeliveryFee(deliveryFee.doubleValue());
        return new RestaurantOperationResponse(true, null, restaurantMapper.toResponse(restaurantRepository.save(restaurant)));
    }

    @Transactional
    public RestaurantOperationResponse updateRestaurantBussinessDetails(String id, BussinessDetailsInput bussinessDetails) {
        RestaurantEntity restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant", id));
        applyBussinessDetails(restaurant, bussinessDetails);
        return new RestaurantOperationResponse(true, null, restaurantMapper.toResponse(restaurantRepository.save(restaurant)));
    }

    @Transactional
    public RestaurantResponse saveRestaurantToken(String token, Boolean isEnabled) {
        String userId = SecurityUtil.getCurrentUserId();
        List<RestaurantEntity> restaurants = restaurantRepository.findByOwnerId(userId);
        if (restaurants.isEmpty()) throw new NotFoundException("No restaurant for this owner.");
        RestaurantEntity restaurant = restaurants.get(0);
        if (token != null) restaurant.setNotificationToken(token);
        if (isEnabled != null) restaurant.setEnableNotification(isEnabled);
        return restaurantMapper.toResponse(restaurantRepository.save(restaurant));
    }

    public Map<String, Object> getRestaurantDeliveryZoneInfo(String id) {
        RestaurantEntity r = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant", id));
        Map<String, Object> result = new HashMap<>();
        result.put("boundType", r.getDeliveryBoundsType() != null ? r.getDeliveryBoundsType() : "Polygon");
        result.put("address", r.getAddress());
        result.put("postCode", r.getPostCode());
        result.put("city", r.getCity());

        if (r.getLat() != null && r.getLng() != null) {
            result.put("location", Map.of("coordinates", List.of(
                    Double.parseDouble(r.getLng()), Double.parseDouble(r.getLat()))));
        }
        if (r.getDeliveryBounds() != null) {
            try {
                result.put("deliveryBounds", Map.of("coordinates", objectMapper.readValue(r.getDeliveryBounds(), Object.class)));
            } catch (Exception ignored) {}
        }
        if (r.getCircleBoundsRadius() != null) {
            result.put("circleBounds", Map.of("radius", r.getCircleBoundsRadius()));
        }
        return result;
    }

    private void applyBussinessDetails(RestaurantEntity restaurant, BussinessDetailsInput details) {
        if (details == null) return;
        restaurant.setBankName(details.getBankName());
        restaurant.setAccountName(details.getAccountName());
        restaurant.setAccountCode(details.getAccountCode());
        restaurant.setAccountNumber(details.getAccountNumber() != null ? String.valueOf(details.getAccountNumber()) : null);
        restaurant.setBussinessRegNo(details.getBussinessRegNo() != null ? String.valueOf(details.getBussinessRegNo()) : null);
        restaurant.setCompanyRegNo(details.getCompanyRegNo() != null ? String.valueOf(details.getCompanyRegNo()) : null);
        restaurant.setTaxRate(details.getTaxRate() != null ? details.getTaxRate().doubleValue() : null);
    }

    private String generateSlug(String name) {
        String base = name.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
        String slug = base;
        int counter = 1;
        while (restaurantRepository.findBySlug(slug).isPresent()) {
            slug = base + "-" + counter++;
        }
        return slug;
    }

    // --------------- Preview / nearBy queries ---------------

    public RestaurantResponse getRestaurantPreview(String id, String slug) {
        return getRestaurant(id, slug);
    }

    public List<RestaurantResponse> getRestaurantListPreview() {
        return getRestaurantList();
    }

    public List<RestaurantResponse> getRestaurantsPreview() {
        return restaurantRepository.findAll().stream()
                .map(restaurantMapper::toResponse).toList();
    }

    public Map<String, Object> nearByRestaurants(Float latitude, Float longitude, String shopType) {
        List<RestaurantEntity> all = restaurantRepository.findAll();
        List<RestaurantResponse> restaurants = all.stream()
                .filter(r -> Boolean.TRUE.equals(r.getIsActive()))
                .filter(r -> shopType == null ||
                        (r.getShopType().getIsActive()
                                && shopType.equals(r.getShopType().getName())))
                .filter(r -> isWithinRadius(r, latitude, longitude, 20.0))
                .map(restaurantMapper::toResponse)
                .toList();

        List<Map<String, Object>> offers = offerRepository.findAll().stream()
                .map(o -> buildOfferInfo(o)).toList();
        List<Map<String, Object>> sections = sectionRepository.findAll().stream()
                .map(s -> buildSectionInfo(s)).toList();

        return Map.of("restaurants", restaurants, "offers", offers, "sections", sections);
    }

    public Map<String, Object> nearByRestaurantsPreview(Float latitude, Float longitude, String shopType, Integer page, Integer limit) {
        int pageSize = limit != null ? limit : 10;
        List<RestaurantEntity> all = restaurantRepository.findAll();
        List<RestaurantResponse> restaurants = all.stream()
                .filter(r -> Boolean.TRUE.equals(r.getIsActive()))
                .filter(r -> shopType == null ||
                        (r.getShopType().getIsActive()
                                && shopType.equals(r.getShopType().getName())))
                //                .filter(r -> isWithinRadius(r, latitude, longitude, 20.0))
                .limit(pageSize)
                .map(restaurantMapper::toResponse)
                .toList();

        List<Map<String, Object>> offers = offerRepository.findAll().stream()
                .map(o -> buildOfferInfo(o)).toList();
        List<Map<String, Object>> sections = sectionRepository.findAll().stream()
                .map(s -> buildSectionInfo(s)).toList();

        return Map.of("restaurants", restaurants, "offers", offers, "sections", sections);
    }

    public List<RestaurantResponse> recentOrderRestaurants(Float latitude, Float longitude) {
        return restaurantRepository.findAll().stream()
                .filter(r -> Boolean.TRUE.equals(r.getIsActive()))
                .filter(r -> isWithinRadius(r, latitude, longitude, 30.0))
                .map(restaurantMapper::toResponse)
                .limit(10)
                .toList();
    }

    public List<RestaurantResponse> recentOrderRestaurantsPreview(Float latitude, Float longitude) {
        return recentOrderRestaurants(latitude, longitude);
    }

    public List<RestaurantResponse> mostOrderedRestaurants(Float latitude, Float longitude) {
        return restaurantRepository.findAll().stream()
                .filter(r -> Boolean.TRUE.equals(r.getIsActive()))
                .filter(r -> isWithinRadius(r, latitude, longitude, 30.0))
                .sorted((a, b) -> Integer.compare(
                        orderRepository.countByRestaurantId(b.getId()),
                        orderRepository.countByRestaurantId(a.getId())))
                .map(restaurantMapper::toResponse)
                .limit(10)
                .toList();
    }

    public List<RestaurantResponse> mostOrderedRestaurantsPreview(Float latitude, Float longitude, String shopType, Integer page, Integer limit) {
        int pageSize = limit != null ? limit : 10;
        return restaurantRepository.findAll().stream()
                .filter(r -> Boolean.TRUE.equals(r.getIsActive()))
                .filter(r -> shopType == null ||
                        (r.getShopType().getIsActive()
                                && shopType.equals(r.getShopType().getName())))                  .filter(r -> isWithinRadius(r, latitude, longitude, 30.0))
                .sorted((a, b) -> Integer.compare(
                        orderRepository.countByRestaurantId(b.getId()),
                        orderRepository.countByRestaurantId(a.getId())))
                .limit(pageSize)
                .map(restaurantMapper::toResponse)
                .toList();
    }

    public List<RestaurantResponse> topRatedVendors(Float latitude, Float longitude) {
        return restaurantRepository.findAll().stream()
                .filter(r -> Boolean.TRUE.equals(r.getIsActive()))
                .filter(r -> isWithinRadius(r, latitude, longitude, 30.0))
                .sorted((a, b) -> Double.compare(
                        safeRating(b.getId()), safeRating(a.getId())))
                .limit(10)
                .map(restaurantMapper::toResponse)
                .toList();
    }

    public List<RestaurantResponse> topRatedVendorsPreview(Float latitude, Float longitude) {
        return topRatedVendors(latitude, longitude);
    }

    public List<Map<String, Object>> getRestaurantMenuItems(String restaurantId) {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant", restaurantId));
        if (restaurant.getCategories() == null) return List.of();
        return restaurant.getCategories().stream().map(cat -> {
            List<Map<String, Object>> children = cat.getSubCategories() != null
                    ? cat.getSubCategories().stream().map(sub -> {
                        List<Map<String, Object>> subFoods = foodRepository.findBySubCategoryId(sub.getId())
                                .stream().map(f -> Map.<String, Object>of(
                                        "_id", f.getId(), "id", f.getId(), "title", f.getTitle()))
                                .toList();
                        return Map.<String, Object>of(
                                "category_name", sub.getTitle(),
                                "category_id", sub.getId(),
                                "products", subFoods
                        );
                    }).toList()
                    : List.of();
            List<Map<String, Object>> products = cat.getFoods() != null
                    ? cat.getFoods().stream().map(f -> Map.<String, Object>of(
                            "_id", f.getId(), "id", f.getId(), "title", f.getTitle()))
                            .toList()
                    : List.of();
            return Map.<String, Object>of(
                    "category_name", cat.getTitle(),
                    "category_id", cat.getId(),
                    "children", children,
                    "products", products
            );
        }).toList();
    }

    public List<String> getRelatedItems(String itemId, String restaurantId) {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant", restaurantId));
        if (restaurant.getCategories() == null) return List.of();
        return restaurant.getCategories().stream()
                .filter(cat -> cat.getFoods() != null && cat.getFoods().stream()
                        .anyMatch(f -> f.getId().equals(itemId)))
                .flatMap(cat -> cat.getFoods().stream())
                .filter(f -> !f.getId().equals(itemId))
                .map(f -> f.getId())
                .limit(10)
                .toList();
    }

    public List<Map<String, Object>> fetchCategoryDetailsByStoreId(String storeId) {
        RestaurantEntity restaurant = restaurantRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException("Restaurant", storeId));
        if (restaurant.getCategories() == null) return List.of();
        return restaurant.getCategories().stream().map(cat -> {
            List<Map<String, Object>> children = cat.getSubCategories() != null
                    ? cat.getSubCategories().stream().map(sub -> Map.<String, Object>of(
                            "id", sub.getId(),
                            "label", sub.getTitle(),
                            "slug", sub.getTitle().toLowerCase().replaceAll("\\s+", "-"),
                            "url", "/category/" + sub.getId(),
                            "items", List.of()
                    )).toList()
                    : List.of();
            return Map.<String, Object>of(
                    "id", cat.getId(),
                    "label", cat.getTitle(),
                    "slug", cat.getTitle().toLowerCase().replaceAll("\\s+", "-"),
                    "url", "/category/" + cat.getId(),
                    "items", children
            );
        }).toList();
    }

    public List<Map<String, Object>> fetchCategoryDetailsByStoreIdForMobile(String storeId) {
        RestaurantEntity restaurant = restaurantRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException("Restaurant", storeId));
        if (restaurant.getCategories() == null) return List.of();
        List<Map<String, Object>> result = new ArrayList<>();
        for (var cat : restaurant.getCategories()) {
            if (cat.getFoods() != null) {
                for (var food : cat.getFoods()) {
                    result.add(Map.of(
                            "id", cat.getId(),
                            "category_name", cat.getTitle(),
                            "url", "/food/" + food.getId(),
                            "food_id", food.getId()
                    ));
                }
            }
        }
        return result;
    }

    public List<Map<String, Object>> getPopularItems(String restaurantId) {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant", restaurantId));
        if (restaurant.getCategories() == null) return List.of();
        return restaurant.getCategories().stream()
                .filter(cat -> cat.getFoods() != null)
                .flatMap(cat -> cat.getFoods().stream())
                .map(food -> {
                    long count = orderRepository.findAll().stream()
                            .filter(o -> o.getItems() != null)
                            .count();
                    return Map.<String, Object>of("id", food.getId(), "count", (int) count);
                })
                .sorted((a, b) -> Integer.compare((int) b.get("count"), (int) a.get("count")))
                .limit(10)
                .toList();
    }

    public List<FoodResponse> getPopularFoodItems(String restaurantId) {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant", restaurantId));
        if (restaurant.getCategories() == null) return List.of();
        return restaurant.getCategories().stream()
                .filter(cat -> cat.getFoods() != null)
                .flatMap(cat -> cat.getFoods().stream())
                .map(food -> FoodResponse.builder()
                        .id(food.getId())
                        .title(food.getTitle())
                        .description(food.getDescription())
                        .image(food.getImage())
                        .isActive(food.getIsActive())
                        .isOutOfStock(food.getIsOutOfStock())
                        .createdAt(food.getCreatedAt() != null ? food.getCreatedAt().toString() : null)
                        .updatedAt(food.getUpdatedAt() != null ? food.getUpdatedAt().toString() : null)
                        .build())
                .limit(10)
                .toList();
    }

    // --------------- Helpers ---------------

    private boolean isWithinRadius(RestaurantEntity r, Float lat, Float lng, double radiusKm) {
        if (lat == null || lng == null || r.getLat() == null || r.getLng() == null) return true;
        try {
            double rLat = Double.parseDouble(r.getLat());
            double rLng = Double.parseDouble(r.getLng());
            double dist = haversine(lat, lng, rLat, rLng);
            return dist <= radiusKm;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    private double safeRating(String restaurantId) {
        Double avg = reviewRepository.findAverageRatingByRestaurantId(restaurantId);
        return avg != null ? avg : 0.0;
    }

    private Map<String, Object> buildOfferInfo(com.celebrate.entity.OfferEntity o) {
        List<String> restaurantIds = o.getRestaurants() != null
                ? o.getRestaurants().stream().map(r -> r.getId()).toList() : List.of();
        return Map.of("_id", o.getId(), "name", o.getName(), "tag", o.getTag(),
                "restaurants", restaurantIds);
    }

    private Map<String, Object> buildSectionInfo(com.celebrate.entity.SectionEntity s) {
        List<String> restaurantIds = s.getRestaurants() != null
                ? s.getRestaurants().stream().map(r -> r.getId()).toList() : List.of();
        return Map.of("_id", s.getId(), "name", s.getName(), "restaurants", restaurantIds);
    }
}
