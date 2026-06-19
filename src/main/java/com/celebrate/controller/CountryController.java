package com.celebrate.controller;

import com.celebrate.dto.response.CountryDropdownResponse;
import com.celebrate.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @QueryMapping
    public List<Map<String, Object>> getCountries() {
        return countryService.getCountries();
    }

    @QueryMapping
    public List<CountryDropdownResponse> getCountriesDropdown() {
        return countryService.getCountriesDropdown();
    }

    @QueryMapping
    public Map<String, Object> getCountryByIso(@Argument String iso) {
        return countryService.getCountryByIso(iso);
    }

    @QueryMapping
    public Map<String, Object> getCitiesByCountry(@Argument String id) {
        return countryService.getCitiesByCountry(id);
    }

    @QueryMapping
    public Map<String, Object> updateCountryFlags() {
        return countryService.updateCountryFlags();
    }
}
