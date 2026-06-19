package com.celebrate.controller;

import com.celebrate.dto.input.TippingInput;
import com.celebrate.dto.response.TippingResponse;
import com.celebrate.service.TippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TippingController {

    private final TippingService tippingService;

    @QueryMapping("tips")
    public TippingResponse tips() {
        return tippingService.getTipping();
    }

    @MutationMapping
    public TippingResponse createTipping(@Argument TippingInput tippingInput) {
        return tippingService.createTipping(tippingInput);
    }

    @MutationMapping
    public TippingResponse editTipping(@Argument TippingInput tippingInput) {
        return tippingService.editTipping(tippingInput);
    }
}
