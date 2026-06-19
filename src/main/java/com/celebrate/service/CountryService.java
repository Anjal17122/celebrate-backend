package com.celebrate.service;

import com.celebrate.dto.response.CountryDropdownResponse;
import com.celebrate.entity.CityEntity;
import com.celebrate.entity.CountryEntity;
import com.celebrate.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;

    public List<Map<String, Object>> getCountries() {
        return countryRepository.findAll().stream().map(this::toMap).toList();
    }

    public List<CountryDropdownResponse> getCountriesDropdown() {
        return countryRepository.findAll().stream().map(c ->
                CountryDropdownResponse.builder()
                        .key(c.getId())
                        .label(c.getName())
                        .value(c.getId())
                        .build()
        ).toList();
    }

    public Map<String, Object> getCountryByIso(String iso) {
        return countryRepository.findByName(iso).map(this::toMap).orElse(null);
    }

    public Map<String, Object> getCitiesByCountry(String id) {
        if (id == null) return null;
        Optional<CountryEntity> country = countryRepository.findById(id);
        return country.map(this::toMap).orElse(null);
    }

    public Map<String, Object> updateCountryFlags() {
        // No external API integration — return placeholder
        return Map.of("name", "flags-updated");
    }

    private Map<String, Object> toMap(CountryEntity c) {
        List<Map<String, Object>> cities = c.getCities() != null
                ? c.getCities().stream().map(this::cityToMap).toList()
                : List.of();
        return Map.of(
                "id", c.getCountryIntId() != null ? c.getCountryIntId() : 0,
                "_id", c.getId(),
                "name", c.getName() != null ? c.getName() : "",
                "latitude", c.getLatitude() != null ? c.getLatitude() : "",
                "longitude", c.getLongitude() != null ? c.getLongitude() : "",
                "flag", c.getFlag() != null ? c.getFlag() : "",
                "cities", cities
        );
    }

    private Map<String, Object> cityToMap(CityEntity city) {
        return Map.of(
                "id", city.getCityIntId() != null ? city.getCityIntId() : 0,
                "name", city.getName() != null ? city.getName() : "",
                "latitude", city.getLatitude() != null ? city.getLatitude() : "",
                "longitude", city.getLongitude() != null ? city.getLongitude() : ""
        );
    }
}
