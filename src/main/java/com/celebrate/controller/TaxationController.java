package com.celebrate.controller;

import com.celebrate.dto.input.TaxationInput;
import com.celebrate.dto.response.TaxationResponse;
import com.celebrate.service.TaxationService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TaxationController {

    private final TaxationService taxationService;

    @QueryMapping("taxes")
    public TaxationResponse taxes() {
        return taxationService.getTaxation();
    }

    @MutationMapping
    public TaxationResponse createTaxation(@Argument TaxationInput taxationInput) {
        return taxationService.createTaxation(taxationInput);
    }

    @MutationMapping
    public TaxationResponse editTaxation(@Argument TaxationInput taxationInput) {
        return taxationService.editTaxation(taxationInput);
    }
}
