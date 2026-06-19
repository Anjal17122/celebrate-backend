package com.celebrate.service;

import com.celebrate.dto.input.BannerInput;
import com.celebrate.dto.response.BannerResponse;
import com.celebrate.entity.BannerEntity;
import com.celebrate.exception.*;
import com.celebrate.mapper.BannerMapper;
import com.celebrate.repository.BannerRepository;
import com.celebrate.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;

    public List<BannerResponse> getAllBanners() {
        return bannerRepository.findAll().stream().map(bannerMapper::toResponse).toList();
    }

    public List<String> getBannerActions() {
        return List.of("navigate", "external", "open", "none");
    }

    @Transactional
    public BannerResponse createBanner(BannerInput input) {
        SecurityUtil.requireRole("ADMIN");
        BannerEntity banner = BannerEntity.builder()
                .title(input.getTitle())
                .description(input.getDescription())
                .file(input.getFile())
                .action(input.getAction())
                .screen(input.getScreen())
                .parameters(input.getParameters())
                .build();
        return bannerMapper.toResponse(bannerRepository.save(banner));
    }

    @Transactional
    public BannerResponse editBanner(BannerInput input) {
        SecurityUtil.requireRole("ADMIN");
        BannerEntity banner = bannerRepository.findById(input.getId())
                .orElseThrow(() -> new NotFoundException("Banner", input.getId()));
        if (input.getTitle() != null) banner.setTitle(input.getTitle());
        if (input.getDescription() != null) banner.setDescription(input.getDescription());
        if (input.getFile() != null) banner.setFile(input.getFile());
        if (input.getAction() != null) banner.setAction(input.getAction());
        if (input.getScreen() != null) banner.setScreen(input.getScreen());
        if (input.getParameters() != null) banner.setParameters(input.getParameters());
        return bannerMapper.toResponse(bannerRepository.save(banner));
    }

    @Transactional
    public String deleteBanner(String id) {
        SecurityUtil.requireRole("ADMIN");
        BannerEntity banner = bannerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Banner", id));
        bannerRepository.delete(banner);
        return "Banner deleted.";
    }

    public BannerResponse getBannerByName(String name) {
        return bannerRepository.findByTitle(name)
                .map(bannerMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Banner", name));
    }
}
