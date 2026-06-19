package com.celebrate.service;

import com.celebrate.dto.input.*;
import com.celebrate.dto.response.*;
import com.celebrate.entity.*;
import com.celebrate.exception.*;
import com.celebrate.mapper.RiderMapper;
import com.celebrate.repository.*;
import com.celebrate.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RiderService {

    private final RiderRepository riderRepository;
    private final ZoneRepository zoneRepository;
    private final DayScheduleRepository dayScheduleRepository;
    private final RiderMapper riderMapper;
    private final PasswordEncoder passwordEncoder;
    private final SubscriptionPublisher subscriptionPublisher;

    public List<RiderResponse> getAllRiders() {
        SecurityUtil.requireRole("ADMIN");
        return riderRepository.findAll().stream().map(riderMapper::toResponse).toList();
    }

    public RiderResponse getRider(String id) {
        return riderMapper.toResponse(riderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rider", id)));
    }

    public List<RiderResponse> getAvailableRiders() {
        return riderRepository.findByAvailableTrue().stream().map(riderMapper::toResponse).toList();
    }

    public List<RiderResponse> getRidersByZone(String zoneId) {
        return riderRepository.findByZoneId(zoneId).stream().map(riderMapper::toResponse).toList();
    }

    @Transactional
    public RiderResponse createRider(RiderInput input) {
        SecurityUtil.requireRole("ADMIN");
        if (riderRepository.existsByUsername(input.getUsername())) {
            throw new ConflictException("A rider with this username already exists.");
        }

        ZoneEntity zone = zoneRepository.findById(input.getZone())
                .orElseThrow(() -> new NotFoundException("Zone", input.getZone()));

        RiderEntity rider = RiderEntity.builder()
                .name(input.getName())
                .email(input.getEmail())
                .username(input.getUsername())
                .password(passwordEncoder.encode(input.getPassword()))
                .phone(input.getPhone())
                .image(input.getImage())
                .available(input.getAvailable() != null ? input.getAvailable() : true)
                .vehicleType(input.getVehicleType())
                .zone(zone)
                .accountNumber(input.getAccountNumber())
                .isActive(true)
                .currentWalletAmount(0.0)
                .totalWalletAmount(0.0)
                .withdrawnWalletAmount(0.0)
                .build();

        applyBussinessDetails(rider, input.getBussinessDetails());
        applyLicenseDetails(rider, input.getLicenseDetails());
        applyVehicleDetails(rider, input.getVehicleDetails());

        return riderMapper.toResponse(riderRepository.save(rider));
    }

    @Transactional
    public RiderResponse editRider(RiderInput input) {
        RiderEntity rider = riderRepository.findById(input.getId())
                .orElseThrow(() -> new NotFoundException("Rider", input.getId()));

        if (input.getName() != null) rider.setName(input.getName());
        if (input.getEmail() != null) rider.setEmail(input.getEmail());
        if (input.getPhone() != null) rider.setPhone(input.getPhone());
        if (input.getImage() != null) rider.setImage(input.getImage());
        if (input.getAvailable() != null) rider.setAvailable(input.getAvailable());
        if (input.getVehicleType() != null) rider.setVehicleType(input.getVehicleType());
        if (input.getAccountNumber() != null) rider.setAccountNumber(input.getAccountNumber());
        if (input.getPassword() != null) rider.setPassword(passwordEncoder.encode(input.getPassword()));

        if (input.getZone() != null) {
            ZoneEntity zone = zoneRepository.findById(input.getZone())
                    .orElseThrow(() -> new NotFoundException("Zone", input.getZone()));
            rider.setZone(zone);
        }

        if (input.getBussinessDetails() != null) applyBussinessDetails(rider, input.getBussinessDetails());
        if (input.getLicenseDetails() != null) applyLicenseDetails(rider, input.getLicenseDetails());
        if (input.getVehicleDetails() != null) applyVehicleDetails(rider, input.getVehicleDetails());

        return riderMapper.toResponse(riderRepository.save(rider));
    }

    @Transactional
    public RiderResponse deleteRider(String id) {
        SecurityUtil.requireRole("ADMIN");
        RiderEntity rider = riderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rider", id));
        riderRepository.delete(rider);
        return riderMapper.toResponse(rider);
    }

    @Transactional
    public RiderResponse toggleAvailability(String id) {
        RiderEntity rider = riderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rider", id));
        rider.setAvailable(!Boolean.TRUE.equals(rider.getAvailable()));
        RiderResponse response = riderMapper.toResponse(riderRepository.save(rider));
        subscriptionPublisher.publishRiderUpdated(response);
        return response;
    }

    @Transactional
    public RiderResponse updateRiderLocation(String latitude, String longitude) {
        String riderId = SecurityUtil.getCurrentUserId();
        RiderEntity rider = riderRepository.findById(riderId)
                .orElseThrow(() -> new NotFoundException("Rider", riderId));
        rider.setLat(latitude);
        rider.setLng(longitude);
        RiderResponse response = riderMapper.toResponse(riderRepository.save(rider));
        subscriptionPublisher.publishRiderLocation(response);
        return response;
    }

    @Transactional
    public RiderResponse updateRiderBussinessDetails(String id, BussinessDetailsInput bussinessDetails) {
        RiderEntity rider = riderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rider", id));
        applyBussinessDetails(rider, bussinessDetails);
        return riderMapper.toResponse(riderRepository.save(rider));
    }

    @Transactional
    public RiderResponse updateRiderLicenseDetails(String id, LicenseDetailsInput licenseDetails) {
        RiderEntity rider = riderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rider", id));
        applyLicenseDetails(rider, licenseDetails);
        return riderMapper.toResponse(riderRepository.save(rider));
    }

    @Transactional
    public RiderResponse updateRiderVehicleDetails(String id, VehicleDetailsInput vehicleDetails) {
        RiderEntity rider = riderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rider", id));
        applyVehicleDetails(rider, vehicleDetails);
        return riderMapper.toResponse(riderRepository.save(rider));
    }

    @Transactional
    public RiderResponse updateWorkSchedule(String riderId, List<DayScheduleInput> workSchedule, String timeZone) {
        RiderEntity rider = riderRepository.findById(riderId)
                .orElseThrow(() -> new NotFoundException("Rider", riderId));

        // Clear existing work schedule
        if (rider.getWorkSchedule() != null) {
            dayScheduleRepository.deleteByRiderId(riderId);
        }

        if (workSchedule != null) {
            List<DayScheduleEntity> daySchedules = new ArrayList<>();
            for (DayScheduleInput dsInput : workSchedule) {
                DayScheduleEntity daySchedule = new DayScheduleEntity();
                daySchedule.setDay(dsInput.getDay());
                daySchedule.setEnabled(dsInput.getEnabled());
                daySchedule.setRider(rider);

                if (dsInput.getSlots() != null) {
                    List<TimeSlotEntity> slots = dsInput.getSlots().stream().map(s -> {
                        TimeSlotEntity slot = new TimeSlotEntity();
                        slot.setStartTime(s.getStartTime());
                        slot.setEndTime(s.getEndTime());
                        slot.setDaySchedule(daySchedule);
                        return slot;
                    }).toList();
                    daySchedule.setSlots(slots);
                }
                daySchedules.add(daySchedule);
            }
            rider.setWorkSchedule(daySchedules);
        }

        rider.setTimeZone(timeZone);
        return riderMapper.toResponse(riderRepository.save(rider));
    }

    public boolean notifyRiders(String orderId) {
        // TODO: push notification to available riders
        return true;
    }

    private void applyBussinessDetails(RiderEntity rider, BussinessDetailsInput details) {
        if (details == null) return;
        rider.setBankName(details.getBankName());
        rider.setAccountName(details.getAccountName());
        rider.setAccountCode(details.getAccountCode());
        rider.setBussinessRegNo(details.getBussinessRegNo() != null ? String.valueOf(details.getBussinessRegNo()) : null);
        rider.setCompanyRegNo(details.getCompanyRegNo() != null ? String.valueOf(details.getCompanyRegNo()) : null);
        rider.setTaxRate(details.getTaxRate() != null ? details.getTaxRate().doubleValue() : null);
        if (details.getAccountNumber() != null) rider.setAccountNumber(String.valueOf(details.getAccountNumber()));
    }

    private void applyLicenseDetails(RiderEntity rider, LicenseDetailsInput details) {
        if (details == null) return;
        rider.setLicenseNumber(details.getNumber());
        rider.setLicenseExpiryDate(details.getExpiryDate() != null ? details.getExpiryDate().toString() : null);
        rider.setLicenseImage(details.getImage());
    }

    private void applyVehicleDetails(RiderEntity rider, VehicleDetailsInput details) {
        if (details == null) return;
        rider.setVehicleNumber(details.getNumber());
        rider.setVehicleImage(details.getImage());
    }
}
