package com.celebrate.service;

import com.celebrate.dto.input.StaffInput;
import com.celebrate.dto.input.VendorInput;
import com.celebrate.dto.response.OwnerAuthDataResponse;
import com.celebrate.dto.response.OwnerResponse;
import com.celebrate.dto.response.RestaurantResponse;
import com.celebrate.dto.response.StaffResponse;
import com.celebrate.entity.OwnerEntity;
import com.celebrate.entity.StaffEntity;
import com.celebrate.exception.*;
import com.celebrate.mapper.OwnerMapper;
import com.celebrate.mapper.StaffMapper;
import com.celebrate.repository.*;
import com.celebrate.security.JwtProvider;
import com.celebrate.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final OwnerRepository ownerRepository;
    private final RestaurantRepository restaurantRepository;
    private final StaffRepository staffRepository;
    private final OwnerMapper ownerMapper;
    private final StaffMapper staffMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public List<OwnerResponse> getVendors() {
        SecurityUtil.requireRole("ADMIN");
        return ownerRepository.findAll().stream()
                .filter(o -> "VENDOR".equals(o.getUserType()))
                .map(ownerMapper::toResponse)
                .toList();
    }

    public OwnerResponse getVendor(String id) {
        SecurityUtil.requireRole("ADMIN");
        OwnerEntity owner = ownerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vendor", id));
        return ownerMapper.toResponse(owner);
    }

    public OwnerResponse restaurantByOwner(String id) {
        String ownerId = id != null ? id : SecurityUtil.getCurrentUserId();
        OwnerEntity owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Vendor", ownerId));
        return ownerMapper.toResponse(owner);
    }

    @Transactional
    public OwnerResponse createVendor(VendorInput input) {
        SecurityUtil.requireRole("ADMIN");
        if (input.getEmail() != null && ownerRepository.existsByEmail(input.getEmail())) {
            throw new ConflictException("A vendor with this email already exists.");
        }
        String uniqueId = "VENDOR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        OwnerEntity owner = OwnerEntity.builder()
                .uniqueId(uniqueId)
                .email(input.getEmail())
                .name(input.getName())
                .image(input.getImage())
                .phoneNumber(input.getPhoneNumber())
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .userType("VENDOR")
                .isActive(true)
                .password(input.getPassword() != null ? passwordEncoder.encode(input.getPassword()) : null)
                .plainPassword(input.getPassword())
                .build();

        return ownerMapper.toResponse(ownerRepository.save(owner));
    }

    @Transactional
    public OwnerResponse editVendor(VendorInput input) {
        String ownerId = input.getId() != null ? input.getId() : SecurityUtil.getCurrentUserId();
        OwnerEntity owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Vendor", ownerId));

        if (input.getEmail() != null) owner.setEmail(input.getEmail());
        if (input.getName() != null) owner.setName(input.getName());
        if (input.getImage() != null) owner.setImage(input.getImage());
        if (input.getPhoneNumber() != null) owner.setPhoneNumber(input.getPhoneNumber());
        if (input.getFirstName() != null) owner.setFirstName(input.getFirstName());
        if (input.getLastName() != null) owner.setLastName(input.getLastName());
        if (input.getPassword() != null) {
            owner.setPassword(passwordEncoder.encode(input.getPassword()));
            owner.setPlainPassword(input.getPassword());
        }

        return ownerMapper.toResponse(ownerRepository.save(owner));
    }

    @Transactional
    public boolean deleteVendor(String id) {
        SecurityUtil.requireRole("ADMIN");
        OwnerEntity owner = ownerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vendor", id));
        ownerRepository.delete(owner);
        return true;
    }

    @Transactional
    public OwnerResponse uploadToken(String id, String pushToken) {
        OwnerEntity owner = ownerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vendor", id));
        owner.setPushToken(pushToken);
        return ownerMapper.toResponse(ownerRepository.save(owner));
    }

    // Staff management
    public List<StaffResponse> getStaffs() {
        SecurityUtil.requireRole("ADMIN", "STAFF");
        return staffRepository.findAll().stream().map(staffMapper::toResponse).toList();
    }

    public StaffResponse getStaff(String id) {
        SecurityUtil.requireRole("ADMIN", "STAFF");
        return staffMapper.toResponse(staffRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Staff", id)));
    }

    @Transactional
    public StaffResponse createStaff(StaffInput input) {
        SecurityUtil.requireRole("ADMIN");
        if (input.getEmail() != null && staffRepository.existsByEmail(input.getEmail())) {
            throw new ConflictException("A staff member with this email already exists.");
        }

        StaffEntity staff = StaffEntity.builder()
                .name(input.getName())
                .email(input.getEmail())
                .password(input.getPassword() != null ? passwordEncoder.encode(input.getPassword()) : null)
                .plainPassword(input.getPassword())
                .phone(input.getPhone())
                .isActive(input.getIsActive() != null ? input.getIsActive() : true)
                .userType(input.getUserType() != null ? input.getUserType() : "STAFF")
                .permissions(input.getPermissions())
                .build();

        return staffMapper.toResponse(staffRepository.save(staff));
    }

    @Transactional
    public StaffResponse editStaff(StaffInput input) {
        StaffEntity staff = staffRepository.findById(input.getId())
                .orElseThrow(() -> new NotFoundException("Staff", input.getId()));

        if (input.getName() != null) staff.setName(input.getName());
        if (input.getEmail() != null) staff.setEmail(input.getEmail());
        if (input.getPhone() != null) staff.setPhone(input.getPhone());
        if (input.getIsActive() != null) staff.setIsActive(input.getIsActive());
        if (input.getPermissions() != null) staff.setPermissions(input.getPermissions());
        if (input.getPassword() != null) {
            staff.setPassword(passwordEncoder.encode(input.getPassword()));
            staff.setPlainPassword(input.getPassword());
        }

        return staffMapper.toResponse(staffRepository.save(staff));
    }

    @Transactional
    public StaffResponse deleteStaff(String id) {
        SecurityUtil.requireRole("ADMIN");
        StaffEntity staff = staffRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Staff", id));
        staffRepository.delete(staff);
        return staffMapper.toResponse(staff);
    }
}
