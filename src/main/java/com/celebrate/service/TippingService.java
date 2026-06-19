package com.celebrate.service;

import com.celebrate.dto.input.TippingInput;
import com.celebrate.dto.response.TippingResponse;
import com.celebrate.entity.TippingEntity;
import com.celebrate.exception.*;
import com.celebrate.repository.TippingRepository;
import com.celebrate.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TippingService {

    private final TippingRepository tippingRepository;

    public TippingResponse getTipping() {
        TippingEntity tipping = tippingRepository.findFirstBy()
                .orElseThrow(() -> new NotFoundException("Tipping configuration not found."));
        return TippingResponse.builder()
                .id(tipping.getId())
                .tipVariations(tipping.getTipVariations())
                .enabled(tipping.getEnabled())
                .build();
    }

    @Transactional
    public TippingResponse createTipping(TippingInput input) {
        SecurityUtil.requireRole("ADMIN");
        TippingEntity tipping = tippingRepository.findFirstBy()
                .orElse(TippingEntity.builder().enabled(false).build());

        if (input.getTipVariations() != null) tipping.setTipVariations(
                input.getTipVariations().stream().map(Float::doubleValue).toList());
        if (input.getEnabled() != null) tipping.setEnabled(input.getEnabled());

        TippingEntity saved = tippingRepository.save(tipping);
        return TippingResponse.builder()
                .id(saved.getId())
                .tipVariations(saved.getTipVariations())
                .enabled(saved.getEnabled())
                .build();
    }

    @Transactional
    public TippingResponse editTipping(TippingInput input) {
        return createTipping(input);
    }
}
