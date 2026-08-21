package com.celebrate.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Service
public class S3Service {

    @Value("${app.storage.mode:s3}")
    private String storageMode;

    @Value("${app.storage.folder:uploads}")
    private String folder;

    @Value("${app.storage.local-path:./uploads}")
    private String localPath;

    @Value("${aws.s3.bucket-name:}")
    private String bucketName;

    @Value("${aws.s3.region:us-east-1}")
    private String region;

    @Value("${aws.s3.access-key:}")
    private String accessKey;

    @Value("${aws.s3.secret-key:}")
    private String secretKey;

    public String uploadBase64Image(String base64, String subfolder) {
        ParsedImage img = parseBase64(base64);
        String ext = contentTypeToExtension(img.contentType);
        String relativePath = folder + "/" + subfolder + "/" + UUID.randomUUID() + "." + ext;

        if ("local".equalsIgnoreCase(storageMode)) {
            saveLocally(relativePath, img.bytes);
        } else {
            saveToS3(relativePath, img.contentType, img.bytes);
        }

        return relativePath;
    }

    public void deleteByRelativePath(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) return;
        if ("local".equalsIgnoreCase(storageMode)) {
            deleteLocally(relativePath);
        } else {
            buildClient().deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(relativePath)
                    .build());
        }
    }

    private void saveLocally(String relativePath, byte[] bytes) {
        try {
            Path target = Paths.get(localPath, relativePath);
            Files.createDirectories(target.getParent());
            Files.write(target, bytes);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to save image locally", e);
        }
    }

    private void deleteLocally(String relativePath) {
        try {
            Files.deleteIfExists(Paths.get(localPath, relativePath));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to delete local image", e);
        }
    }

    private void saveToS3(String key, String contentType, byte[] bytes) {
        buildClient().putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(contentType)
                        .contentLength((long) bytes.length)
                        .build(),
                RequestBody.fromBytes(bytes));
    }

    private ParsedImage parseBase64(String base64) {
        String contentType = "image/jpeg";
        String data = base64;
        if (base64.contains(",")) {
            String[] parts = base64.split(",", 2);
            String header = parts[0];
            data = parts[1];
            if (header.contains(":") && header.contains(";")) {
                contentType = header.split(":")[1].split(";")[0];
            }
        }
        return new ParsedImage(contentType, Base64.getDecoder().decode(data));
    }

    private S3Client buildClient() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    private String contentTypeToExtension(String contentType) {
        return switch (contentType) {
            case "image/jpeg", "image/jpg" -> "jpg";
            case "image/png" -> "png";
            case "image/gif" -> "gif";
            case "image/webp" -> "webp";
            default -> "jpg";
        };
    }

    private record ParsedImage(String contentType, byte[] bytes) {}
}
