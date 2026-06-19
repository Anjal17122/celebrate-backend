package com.celebrate.service;

import com.celebrate.dto.input.AddressInput;
import com.celebrate.dto.input.UpdateUserInput;
import com.celebrate.dto.response.RestaurantResponse;
import com.celebrate.dto.response.SaveNotificationTokenWebResponse;
import com.celebrate.dto.response.UserResponse;
import com.celebrate.entity.AddressEntity;
import com.celebrate.entity.UserEntity;
import com.celebrate.exception.*;
import com.celebrate.mapper.RestaurantMapper;
import com.celebrate.mapper.UserMapper;
import com.celebrate.repository.*;
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
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final NotificationRepository notificationRepository;
    private final WebNotificationRepository webNotificationRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    private final UserMapper userMapper;

    public UserResponse getProfile() {
        String userId = SecurityUtil.getCurrentUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse updateUser(UpdateUserInput input) {
        String userId = SecurityUtil.getCurrentUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));

        if (input.getName() != null) user.setName(input.getName());
        if (input.getPhone() != null) user.setPhone(input.getPhone());
        if (input.getPhoneIsVerified() != null) user.setPhoneIsVerified(input.getPhoneIsVerified());
        if (input.getEmailIsVerified() != null) user.setEmailIsVerified(input.getEmailIsVerified());

        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse updateNotificationStatus(Boolean offerNotification, Boolean orderNotification) {
        String userId = SecurityUtil.getCurrentUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));
        user.setIsOfferNotification(offerNotification);
        user.setIsOrderNotification(orderNotification);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse pushToken(String token) {
        String userId = SecurityUtil.getCurrentUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));
        user.setNotificationToken(token);
        return userMapper.toResponse(userRepository.save(user));
    }

    // Admin operations
    public List<UserResponse> getUsers(Integer page, Integer limit) {
        SecurityUtil.requireRole("ADMIN");
        int pageNum = page != null ? Math.max(0, page - 1) : 0;
        int pageSize = limit != null ? limit : 10;
        return userRepository.findAll(PageRequest.of(pageNum, pageSize))
                .stream().map(userMapper::toResponse).toList();
    }

    public UserResponse getUserById(String id) {
        SecurityUtil.requireRole("ADMIN");
        return userMapper.toResponse(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id)));
    }

    public Map<String, Object> getUsersPaginated(Integer page, Integer limit, String search) {
        SecurityUtil.requireRole("ADMIN");
        int pageNum = page != null ? Math.max(0, page - 1) : 0;
        int pageSize = limit != null ? limit : 10;
        Page<UserEntity> result = userRepository.findAllWithSearch(search, PageRequest.of(pageNum, pageSize));
        return Map.of(
                "users", result.getContent().stream().map(userMapper::toResponse).toList(),
                "docsCount", result.getTotalElements(),
                "totalPages", result.getTotalPages(),
                "currentPage", pageNum + 1
        );
    }

    @Transactional
    public UserResponse updateUserStatus(String id, String status, String reason) {
        SecurityUtil.requireRole("ADMIN");
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));
        user.setStatus(status);
        if (reason != null) user.setStatusReason(reason);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse updateUserNotes(String id, String notes) {
        SecurityUtil.requireRole("ADMIN");
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));
        user.setNotes(notes);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse deleteUser(String id) {
        SecurityUtil.requireRole("ADMIN");
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));
        userRepository.delete(user);
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse deactivate(Boolean isActive, String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("No account with email: " + email));
        user.setIsActive(isActive);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse resetUserSession(String userId) {
        SecurityUtil.requireRole("ADMIN");
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));
        user.setNotificationToken(null);
        user.setLastLogin(null);
        return userMapper.toResponse(userRepository.save(user));
    }

    public UserResponse emailExist(String email) {
        return userRepository.findByEmail(email).map(userMapper::toResponse).orElse(null);
    }

    public UserResponse phoneExist(String phone) {
        return userRepository.findByPhone(phone).map(userMapper::toResponse).orElse(null);
    }

    // Address management
    @Transactional
    public UserResponse createAddress(AddressInput input) {
        String userId = SecurityUtil.getCurrentUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));

        // Deselect all if this is being selected
        if (user.getAddresses() != null) {
            user.getAddresses().forEach(a -> a.setSelected(false));
        }

        AddressEntity address = AddressEntity.builder()
                .user(user)
                .lat(input.getLatitude())
                .lng(input.getLongitude())
                .deliveryAddress(input.getDeliveryAddress())
                .details(input.getDetails())
                .label(input.getLabel())
                .selected(true)
                .build();

        addressRepository.save(address);
        return userMapper.toResponse(userRepository.findById(userId).orElseThrow());
    }

    @Transactional
    public UserResponse editAddress(AddressInput input) {
        String userId = SecurityUtil.getCurrentUserId();
        AddressEntity address = addressRepository.findById(input.getId())
                .orElseThrow(() -> new NotFoundException("Address", input.getId()));

        if (!address.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You cannot edit this address.");
        }

        address.setLat(input.getLatitude());
        address.setLng(input.getLongitude());
        address.setDeliveryAddress(input.getDeliveryAddress());
        address.setDetails(input.getDetails());
        address.setLabel(input.getLabel());
        addressRepository.save(address);

        return userMapper.toResponse(userRepository.findById(userId).orElseThrow());
    }

    @Transactional
    public UserResponse deleteAddress(String id) {
        String userId = SecurityUtil.getCurrentUserId();
        AddressEntity address = addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Address", id));

        if (!address.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You cannot delete this address.");
        }

        addressRepository.delete(address);
        return userMapper.toResponse(userRepository.findById(userId).orElseThrow());
    }

    @Transactional
    public UserResponse deleteBulkAddresses(List<String> ids) {
        String userId = SecurityUtil.getCurrentUserId();
        addressRepository.deleteByIdInAndUserId(ids, userId);
        return userMapper.toResponse(userRepository.findById(userId).orElseThrow());
    }

    @Transactional
    public UserResponse selectAddress(String id) {
        String userId = SecurityUtil.getCurrentUserId();
        List<AddressEntity> addresses = addressRepository.findByUserId(userId);
        addresses.forEach(a -> a.setSelected(a.getId().equals(id)));
        addressRepository.saveAll(addresses);
        return userMapper.toResponse(userRepository.findById(userId).orElseThrow());
    }

    @Transactional
    public UserResponse addFavourite(String restaurantId) {
        String userId = SecurityUtil.getCurrentUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));

        List<String> favs = user.getFavourite();
        if (favs == null) {
            favs = new java.util.ArrayList<>();
            user.setFavourite(favs);
        }

        if (favs.contains(restaurantId)) {
            favs.remove(restaurantId);
        } else {
            favs.add(restaurantId);
        }

        return userMapper.toResponse(userRepository.save(user));
    }

    public List<RestaurantResponse> getUserFavourite(Float latitude, Float longitude) {
        String userId = SecurityUtil.getCurrentUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));
        List<String> favIds = user.getFavourite();
        if (favIds == null || favIds.isEmpty()) return List.of();
        return restaurantRepository.findAllById(favIds).stream()
                .map(restaurantMapper::toResponse)
                .toList();
    }

    public SaveNotificationTokenWebResponse saveNotificationTokenWeb(String token) {
        String userId = SecurityUtil.getCurrentUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));
        user.setNotificationToken(token);
        userRepository.save(user);
        return new SaveNotificationTokenWebResponse(true, "Token saved successfully.");
    }
}
