package com.celebrate.utils;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ImageUrlHelper {

    @Value("${app.storage.base-url}")
    private String baseUrl;

    @Named("resolveImageUrl")
    public String resolve(String path) {
        if (path == null || path.isBlank()) return path;
        if (path.startsWith("http://") || path.startsWith("https://")) return path;
        String base = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        return base + "/" + path;
    }

    @Named("resolveImageUrls")
    public List<String> resolveImageUrls(List<String> paths) {
        if (paths == null) return null;
        return paths.stream().map(this::resolve).toList();
    }

    @Named("toRelativePath")
    public String toRelativePath(String url) {
        if (url == null || url.isBlank()) return url;
        if (!url.startsWith("http://") && !url.startsWith("https://")) return url;
        String base = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
        if (url.startsWith(base)) {
            return url.substring(base.length());
        }
        return url;
    }
}
