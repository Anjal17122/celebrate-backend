package com.celebrate.controller;

import com.celebrate.dto.input.ZoneInput;
import com.celebrate.dto.response.ZoneResponse;
import com.celebrate.service.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ZoneController {

    private final ZoneService zoneService;

    @QueryMapping
    public List<ZoneResponse> zones() {
        return zoneService.getAllZones();
    }

    @QueryMapping
    public ZoneResponse zone(@Argument String id) {
        return zoneService.getZone(id);
    }

    @MutationMapping
    public ZoneResponse createZone(@Argument("zone") ZoneInput zone) {
        return zoneService.createZone(zone);
    }

    @MutationMapping
    public ZoneResponse editZone(@Argument("zone") ZoneInput zone) {
        return zoneService.editZone(zone);
    }

    @MutationMapping
    public ZoneResponse deleteZone(@Argument String id) {
        return zoneService.deleteZone(id);
    }
}
