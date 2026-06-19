package com.celebrate.controller;

import com.celebrate.dto.input.DateFilter;
import com.celebrate.dto.input.EarningsInput;
import com.celebrate.dto.input.PaginationInput;
import com.celebrate.dto.response.EarningsResponse;
import com.celebrate.dto.response.WithdrawRequestResponse;
import com.celebrate.service.EarningsService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class EarningsController {

    private final EarningsService earningsService;

    @QueryMapping
    public Map<String, Object> earnings(
            @Argument String userType,
            @Argument String userId,
            @Argument String orderType,
            @Argument String paymentMethod,
            @Argument PaginationInput pagination,
            @Argument DateFilter dateFilter,
            @Argument String search) {
        return earningsService.getEarnings(userType, userId, orderType, paymentMethod, pagination, dateFilter, search);
    }

    @QueryMapping
    public List<EarningsResponse> riderEarnings(@Argument String id, @Argument Integer offset) {
        return earningsService.getRiderEarnings(id, offset);
    }

    @QueryMapping
    public List<WithdrawRequestResponse> riderWithdrawRequests(@Argument String id, @Argument Integer offset) {
        return earningsService.getRiderWithdrawRequests(id, offset);
    }

    @QueryMapping
    public WithdrawRequestResponse riderCurrentWithdrawRequest(@Argument String riderId) {
        return earningsService.getRiderCurrentWithdrawRequest(riderId);
    }

    @QueryMapping
    public WithdrawRequestResponse storeCurrentWithdrawRequest(@Argument String storeId) {
        return earningsService.getStoreCurrentWithdrawRequest(storeId);
    }

    @QueryMapping
    public Map<String, Object> getAllWithdrawRequests(@Argument Integer offset) {
        return earningsService.getAllWithdrawRequests(offset);
    }

    @QueryMapping
    public Map<String, Object> withdrawRequests(
            @Argument String userType,
            @Argument String userId,
            @Argument PaginationInput pagination,
            @Argument String search) {
        return earningsService.getWithdrawRequests(userType, userId, pagination, search);
    }

    @MutationMapping
    public EarningsResponse createEarning(@Argument EarningsInput earningsInput) {
        return earningsService.createEarning(earningsInput);
    }

    @MutationMapping
    public WithdrawRequestResponse createWithdrawRequest(@Argument Float requestAmount, @Argument String userId) {
        return earningsService.createWithdrawRequest(requestAmount, userId);
    }

    // Schema name: updateWithdrawReqStatus
    @MutationMapping("updateWithdrawReqStatus")
    public Map<String, Object> updateWithdrawReqStatus(@Argument String id, @Argument String status) {
        return earningsService.updateWithdrawRequestStatus(id, status);
    }

    @QueryMapping
    public Map<String, Object> riderWithdrawRequestsHistory(@Argument String userId, @Argument PaginationInput pagination) {
        return earningsService.getRiderWithdrawRequestsHistory(userId, pagination);
    }

    @QueryMapping
    public Map<String, Object> transactionHistory(
            @Argument String userType,
            @Argument String userId,
            @Argument PaginationInput pagination,
            @Argument String search,
            @Argument DateFilter dateFilter) {
        return earningsService.getTransactionHistory(userType, userId, pagination, search, dateFilter);
    }

    @QueryMapping
    public Map<String, Object> riderEarningsGraph(
            @Argument String riderId,
            @Argument Integer page,
            @Argument Integer limit,
            @Argument String startDate,
            @Argument String endDate) {
        return earningsService.getRiderEarningsGraph(riderId, page, limit, startDate, endDate);
    }

    @QueryMapping
    public Map<String, Object> storeEarningsGraph(
            @Argument String storeId,
            @Argument Integer page,
            @Argument Integer limit,
            @Argument String startDate,
            @Argument String endDate) {
        return earningsService.getStoreEarningsGraph(storeId, page, limit, startDate, endDate);
    }
}
