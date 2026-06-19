package com.celebrate.service;

import com.celebrate.dto.input.EarningsInput;
import com.celebrate.dto.input.PaginationInput;
import com.celebrate.dto.input.DateFilter;
import com.celebrate.dto.response.EarningsResponse;
import com.celebrate.dto.response.WithdrawRequestResponse;
import com.celebrate.entity.*;
import com.celebrate.exception.*;
import com.celebrate.mapper.EarningsMapper;
import com.celebrate.repository.*;
import com.celebrate.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EarningsService {

    private final EarningsRepository earningsRepository;
    private final WithdrawRequestRepository withdrawRequestRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final RiderRepository riderRepository;
    private final RestaurantRepository restaurantRepository;
    private final EarningsMapper earningsMapper;

    @Transactional
    public EarningsResponse createEarning(EarningsInput input) {
        RiderEntity rider = riderRepository.findById(input.getRider())
                .orElseThrow(() -> new NotFoundException("Rider", input.getRider()));

        EarningsEntity earning = EarningsEntity.builder()
                .orderId(input.getOrderId())
                .paymentMethod(input.getPaymentMethod())
                .orderType("DELIVERY")
                .rider(rider)
                .riderDeliveryFee(input.getDeliveryFee())
                .riderTip(0.0)
                .riderTotalEarnings(input.getDeliveryFee())
                .marketplaceCommission(0.0)
                .deliveryCommission(0.0)
                .tax(0.0)
                .platformFee(0.0)
                .platformTotalEarnings(0.0)
                .build();

        EarningsEntity saved = earningsRepository.save(earning);

        // Update rider wallet
        rider.setCurrentWalletAmount((rider.getCurrentWalletAmount() != null ? rider.getCurrentWalletAmount() : 0.0) + input.getDeliveryFee());
        rider.setTotalWalletAmount((rider.getTotalWalletAmount() != null ? rider.getTotalWalletAmount() : 0.0) + input.getDeliveryFee());
        riderRepository.save(rider);

        return earningsMapper.toResponse(saved);
    }

    public List<EarningsResponse> getRiderEarnings(String riderId, Integer offset) {
        List<EarningsEntity> earnings = riderId != null
                ? earningsRepository.findByRiderId(riderId)
                : earningsRepository.findByRiderId(SecurityUtil.getCurrentUserId());
        return earnings.stream().map(earningsMapper::toResponse).toList();
    }

    public Map<String, Object> getEarnings(String userType, String userId, String orderType,
                                            String paymentMethod, PaginationInput pagination,
                                            DateFilter dateFilter, String search) {
        int pageNum = pagination != null && pagination.getPageNo() != null ? Math.max(0, pagination.getPageNo() - 1) : 0;
        int pageSize = pagination != null && pagination.getPageSize() != null ? pagination.getPageSize() : 10;

        Page<EarningsEntity> result;
        if ("RIDER".equals(userType) && userId != null) {
            result = earningsRepository.findByRiderIdPaginated(userId, PageRequest.of(pageNum, pageSize, Sort.by("createdAt").descending()));
        } else if ("STORE".equals(userType) && userId != null) {
            result = earningsRepository.findByStoreIdPaginated(userId, PageRequest.of(pageNum, pageSize, Sort.by("createdAt").descending()));
        } else {
            result = earningsRepository.findAll(PageRequest.of(pageNum, pageSize, Sort.by("createdAt").descending()));
        }

        double platformTotal = result.getContent().stream().mapToDouble(e -> e.getPlatformTotalEarnings() != null ? e.getPlatformTotalEarnings() : 0.0).sum();
        double riderTotal = result.getContent().stream().mapToDouble(e -> e.getRiderTotalEarnings() != null ? e.getRiderTotalEarnings() : 0.0).sum();
        double storeTotal = result.getContent().stream().mapToDouble(e -> e.getStoreTotalEarnings() != null ? e.getStoreTotalEarnings() : 0.0).sum();

        return Map.of(
                "success", true,
                "data", Map.of(
                        "earnings", result.getContent().stream().map(earningsMapper::toResponse).toList(),
                        "grandTotalEarnings", Map.of(
                                "platformTotal", platformTotal,
                                "riderTotal", riderTotal,
                                "storeTotal", storeTotal
                        )
                ),
                "pagination", Map.of("total", result.getTotalElements())
        );
    }

    // Withdraw requests
    @Transactional
    public WithdrawRequestResponse createWithdrawRequest(Float requestAmount, String userId) {
        String id = userId != null ? userId : SecurityUtil.getCurrentUserId();

        WithdrawRequestEntity request = WithdrawRequestEntity.builder()
                .requestId("WR-" + System.currentTimeMillis())
                .requestAmount(requestAmount.doubleValue())
                .requestTime(java.time.LocalDateTime.now().toString())
                .status("PENDING")
                .build();

        // Determine if rider or store
        riderRepository.findById(id).ifPresent(request::setRider);
        restaurantRepository.findById(id).ifPresent(request::setStore);

        return mapWithdrawRequest(withdrawRequestRepository.save(request));
    }

    public List<WithdrawRequestResponse> getRiderWithdrawRequests(String riderId, Integer offset) {
        return withdrawRequestRepository.findByRiderId(riderId != null ? riderId : SecurityUtil.getCurrentUserId())
                .stream().map(this::mapWithdrawRequest).toList();
    }

    public WithdrawRequestResponse getRiderCurrentWithdrawRequest(String riderId) {
        return withdrawRequestRepository.findTopByRiderIdAndStatusOrderByCreatedAtDesc(
                        riderId, "PENDING")
                .map(this::mapWithdrawRequest)
                .orElse(null);
    }

    public WithdrawRequestResponse getStoreCurrentWithdrawRequest(String storeId) {
        return withdrawRequestRepository.findTopByStoreIdAndStatusOrderByCreatedAtDesc(
                        storeId, "PENDING")
                .map(this::mapWithdrawRequest)
                .orElse(null);
    }

    public Map<String, Object> getWithdrawRequests(String userType, String userId, PaginationInput pagination, String search) {
        int pageNum = pagination != null && pagination.getPageNo() != null ? Math.max(0, pagination.getPageNo() - 1) : 0;
        int pageSize = pagination != null && pagination.getPageSize() != null ? pagination.getPageSize() : 10;

        Page<WithdrawRequestEntity> result = withdrawRequestRepository.findAll(
                PageRequest.of(pageNum, pageSize, Sort.by("createdAt").descending()));

        return Map.of(
                "success", true,
                "data", result.getContent().stream().map(this::mapWithdrawRequest).toList(),
                "pagination", Map.of("total", result.getTotalElements())
        );
    }

    public Map<String, Object> getAllWithdrawRequests(Integer offset) {
        SecurityUtil.requireRole("ADMIN");
        List<WithdrawRequestEntity> all = withdrawRequestRepository.findAll(Sort.by("createdAt").descending());
        return Map.of(
                "success", true,
                "data", all.stream().map(this::mapWithdrawRequest).toList(),
                "pagination", Map.of("total", all.size())
        );
    }

    @Transactional
    public Map<String, Object> updateWithdrawRequestStatus(String id, String status) {
        SecurityUtil.requireRole("ADMIN");
        WithdrawRequestEntity request = withdrawRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("WithdrawRequest", id));
        request.setStatus(status);

        if ("approved".equals(status)) {
            // Deduct from wallet
            if (request.getRider() != null) {
                RiderEntity rider = request.getRider();
                double newAmount = (rider.getCurrentWalletAmount() != null ? rider.getCurrentWalletAmount() : 0.0)
                        - request.getRequestAmount();
                rider.setCurrentWalletAmount(Math.max(0, newAmount));
                rider.setWithdrawnWalletAmount((rider.getWithdrawnWalletAmount() != null ? rider.getWithdrawnWalletAmount() : 0.0)
                        + request.getRequestAmount());
                riderRepository.save(rider);
            }
        }

        WithdrawRequestEntity saved = withdrawRequestRepository.save(request);
        return Map.of("success", true, "data", mapWithdrawRequest(saved));
    }

    private WithdrawRequestResponse mapWithdrawRequest(WithdrawRequestEntity r) {
        return WithdrawRequestResponse.builder()
                .id(r.getId())
                .requestId(r.getRequestId())
                .requestAmount(r.getRequestAmount())
                .requestTime(r.getRequestTime())
                .status(r.getStatus())
                .createdAt(r.getCreatedAt() != null ? r.getCreatedAt().toString() : null)
                .build();
    }

    public Map<String, Object> getRiderWithdrawRequestsHistory(String userId, com.celebrate.dto.input.PaginationInput pagination) {
        int pageNum = pagination != null && pagination.getPageNo() != null ? Math.max(0, pagination.getPageNo() - 1) : 0;
        int pageSize = pagination != null && pagination.getPageSize() != null ? pagination.getPageSize() : 10;

        Page<WithdrawRequestEntity> result = withdrawRequestRepository.findByUserTypeAndUserId(
                "RIDER", userId, PageRequest.of(pageNum, pageSize, Sort.by("createdAt").descending()));

        List<Map<String, Object>> data = result.getContent().stream().map(r -> {
            Map<String, Object> m = new HashMap<>();
            m.put("_id", r.getId());
            m.put("status", r.getStatus() != null ? r.getStatus() : "");
            m.put("amountTransferred", r.getAmountTransferred() != null ? r.getAmountTransferred() : 0.0);
            m.put("createdAt", r.getCreatedAt() != null ? r.getCreatedAt().toString() : null);
            return m;
        }).toList();

        return Map.of(
                "success", true,
                "data", data,
                "message", "",
                "pagination", Map.of("total", result.getTotalElements())
        );
    }

    public Map<String, Object> getTransactionHistory(String userType, String userId,
                                                      com.celebrate.dto.input.PaginationInput pagination,
                                                      String search, com.celebrate.dto.input.DateFilter dateFilter) {
        int pageNum = pagination != null && pagination.getPageNo() != null ? Math.max(0, pagination.getPageNo() - 1) : 0;
        int pageSize = pagination != null && pagination.getPageSize() != null ? pagination.getPageSize() : 10;

        Page<TransactionHistoryEntity> result = transactionHistoryRepository.findFiltered(
                userType, userId, search,
                PageRequest.of(pageNum, pageSize, Sort.by("createdAt").descending()));

        List<Map<String, Object>> data = result.getContent().stream().map(t -> {
            Map<String, Object> bankMap = Map.of(
                    "accountName", t.getBankAccountName() != null ? t.getBankAccountName() : "",
                    "bankName", t.getBankName() != null ? t.getBankName() : "",
                    "accountNumber", t.getBankAccountNumber() != null ? t.getBankAccountNumber() : "",
                    "accountCode", t.getBankAccountCode() != null ? t.getBankAccountCode() : ""
            );
            Map<String, Object> m = new HashMap<>();
            m.put("_id", t.getId());
            m.put("amountCurrency", t.getAmountCurrency() != null ? t.getAmountCurrency() : "");
            m.put("status", t.getStatus() != null ? t.getStatus() : "");
            m.put("transactionId", t.getTransactionId() != null ? t.getTransactionId() : "");
            m.put("userType", t.getUserType() != null ? t.getUserType() : "");
            m.put("userId", t.getUserId() != null ? t.getUserId() : "");
            m.put("amountTransferred", t.getAmountTransferred() != null ? t.getAmountTransferred() : 0.0);
            m.put("toBank", bankMap);
            m.put("createdAt", t.getCreatedAt() != null ? t.getCreatedAt().toString() : null);
            return m;
        }).toList();

        return Map.of(
                "success", true,
                "data", data,
                "message", "",
                "pagination", Map.of("total", result.getTotalElements())
        );
    }

    public Map<String, Object> getRiderEarningsGraph(String riderId, Integer page, Integer limit,
                                                      String startDate, String endDate) {
        List<EarningsEntity> earnings = earningsRepository.findByRiderId(riderId);

        Map<String, List<EarningsEntity>> byDate = new LinkedHashMap<>();
        for (EarningsEntity e : earnings) {
            String date = e.getCreatedAt() != null ? e.getCreatedAt().toLocalDate().toString() : "unknown";
            byDate.computeIfAbsent(date, k -> new ArrayList<>()).add(e);
        }

        List<Map<String, Object>> graphData = byDate.entrySet().stream().map(entry -> {
            List<EarningsEntity> group = entry.getValue();
            double totalEarnings = group.stream().mapToDouble(e -> e.getRiderTotalEarnings() != null ? e.getRiderTotalEarnings() : 0.0).sum();
            double totalTips = group.stream().mapToDouble(e -> e.getRiderTip() != null ? e.getRiderTip() : 0.0).sum();
            Map<String, Object> m = new HashMap<>();
            m.put("_id", entry.getKey());
            m.put("earningsArray", List.of());
            m.put("totalEarningsSum", totalEarnings);
            m.put("totalTipsSum", totalTips);
            m.put("totalHours", 0.0);
            m.put("totalCount", group.size());
            m.put("totalDeliveries", group.size());
            m.put("date", entry.getKey());
            return m;
        }).toList();

        int pageSize = limit != null ? limit : 10;
        int fromIdx = page != null ? Math.max(0, page - 1) * pageSize : 0;
        int toIdx = Math.min(fromIdx + pageSize, graphData.size());
        List<Map<String, Object>> paged = fromIdx < graphData.size() ? graphData.subList(fromIdx, toIdx) : List.of();

        return Map.of("earnings", paged, "totalCount", graphData.size());
    }

    public Map<String, Object> getStoreEarningsGraph(String storeId, Integer page, Integer limit,
                                                      String startDate, String endDate) {
        List<EarningsEntity> earnings = earningsRepository.findByStoreId(storeId);

        Map<String, List<EarningsEntity>> byDate = new LinkedHashMap<>();
        for (EarningsEntity e : earnings) {
            String date = e.getCreatedAt() != null ? e.getCreatedAt().toLocalDate().toString() : "unknown";
            byDate.computeIfAbsent(date, k -> new ArrayList<>()).add(e);
        }

        List<Map<String, Object>> graphData = byDate.entrySet().stream().map(entry -> {
            List<EarningsEntity> group = entry.getValue();
            double totalEarnings = group.stream().mapToDouble(e -> e.getStoreTotalEarnings() != null ? e.getStoreTotalEarnings() : 0.0).sum();
            Map<String, Object> m = new HashMap<>();
            m.put("_id", entry.getKey());
            m.put("earningsArray", List.of());
            m.put("totalEarningsSum", totalEarnings);
            m.put("totalCount", group.size());
            m.put("date", entry.getKey());
            return m;
        }).toList();

        int pageSize = limit != null ? limit : 10;
        int fromIdx = page != null ? Math.max(0, page - 1) * pageSize : 0;
        int toIdx = Math.min(fromIdx + pageSize, graphData.size());
        List<Map<String, Object>> paged = fromIdx < graphData.size() ? graphData.subList(fromIdx, toIdx) : List.of();

        return Map.of("earnings", paged, "totalCount", graphData.size());
    }
}
