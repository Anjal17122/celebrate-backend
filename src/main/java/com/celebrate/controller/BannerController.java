package com.celebrate.controller;

import com.celebrate.dto.input.BannerInput;
import com.celebrate.dto.response.BannerResponse;
import com.celebrate.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @QueryMapping
    public List<BannerResponse> banners() {
        return bannerService.getAllBanners();
    }

    @MutationMapping
    public BannerResponse createBanner(@Argument BannerInput bannerInput) {
        return bannerService.createBanner(bannerInput);
    }

    @MutationMapping
    public BannerResponse editBanner(@Argument BannerInput bannerInput) {
        return bannerService.editBanner(bannerInput);
    }

    @MutationMapping
    public String deleteBanner(@Argument String id) {
        return bannerService.deleteBanner(id);
    }

    // Schema: banner(banner: String!): Banner! in Mutation block
    @MutationMapping
    public BannerResponse banner(@Argument String banner) {
        return bannerService.getBannerByName(banner);
    }

    @QueryMapping
    public List<String> bannerActions() {
        return bannerService.getBannerActions();
    }
}
