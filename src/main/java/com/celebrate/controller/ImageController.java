package com.celebrate.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class ImageController {

    @Value("${app.storage.local-path:./uploads}")
    private String localPath;

    @Value("${app.storage.folder:uploads}")
    private String folder;

    // Matches /uploads/**, /restaurants/**, etc. — whatever folder is configured
    @GetMapping("/${app.storage.folder:uploads}/**")
    public ResponseEntity<byte[]> getImage(HttpServletRequest request) throws IOException {
        // request URI: /uploads/general/uuid.jpg  →  relative path: uploads/general/uuid.jpg
        String relativePath = request.getRequestURI().replaceFirst("^/", "");

        Path base = Paths.get(localPath).toAbsolutePath().normalize();
        Path target = base.resolve(relativePath).normalize();

        // prevent path traversal
        if (!target.startsWith(base)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (!Files.exists(target) || !Files.isRegularFile(target)) {
            return ResponseEntity.notFound().build();
        }

        byte[] bytes = Files.readAllBytes(target);
        String contentType = Files.probeContentType(target);
        MediaType mediaType = contentType != null
                ? MediaType.parseMediaType(contentType)
                : MediaType.APPLICATION_OCTET_STREAM;

        return ResponseEntity.ok().contentType(mediaType).body(bytes);
    }
}
