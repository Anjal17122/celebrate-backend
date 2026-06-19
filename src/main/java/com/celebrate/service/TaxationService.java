package com.celebrate.service;

import com.celebrate.dto.input.TaxationInput;
import com.celebrate.dto.response.TaxationResponse;
import com.celebrate.entity.TaxationEntity;
import com.celebrate.exception.*;
import com.celebrate.repository.TaxationRepository;
import com.celebrate.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaxationService {

    private final TaxationRepository taxationRepository;

    public TaxationResponse getTaxation() {
        TaxationEntity taxation = taxationRepository.findFirstBy()
                .orElseThrow(() -> new NotFoundException("Taxation configuration not found."));
        return TaxationResponse.builder()
                .id(taxation.getId())
                .taxationCharges(taxation.getTaxationCharges())
                .enabled(taxation.getEnabled())
                .build();
    }

    @Transactional
    public TaxationResponse createTaxation(TaxationInput input) {
        SecurityUtil.requireRole("ADMIN");
        TaxationEntity taxation = taxationRepository.findFirstBy()
                .orElse(TaxationEntity.builder().enabled(false).build());

        if (input.getTaxationCharges() != null) taxation.setTaxationCharges(input.getTaxationCharges().doubleValue());
        if (input.getEnabled() != null) taxation.setEnabled(input.getEnabled());

        TaxationEntity saved = taxationRepository.save(taxation);
        return TaxationResponse.builder()
                .id(saved.getId())
                .taxationCharges(saved.getTaxationCharges())
                .enabled(saved.getEnabled())
                .build();
    }

    @Transactional
    public TaxationResponse editTaxation(TaxationInput input) {
        return createTaxation(input);
    }
}
