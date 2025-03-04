package com.recovery.managementsystem.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.*;

@RestController
@RequestMapping("/files")
public class FileController {

    private final Path DOCUMENTS_DIR = Paths.get("uploads/documents").toAbsolutePath().normalize();
    private final Path PROFILE_DIR = Paths.get("uploads/profile").toAbsolutePath().normalize();

    @GetMapping("/{type}/{filename}")
    public ResponseEntity<Resource> serveFile(@PathVariable String type, @PathVariable String filename) {
        try {
            Path baseDir;
            if ("documents".equalsIgnoreCase(type)) {
                baseDir = DOCUMENTS_DIR;
            } else if ("profile".equalsIgnoreCase(type)) {
                baseDir = PROFILE_DIR;
            } else {
                return ResponseEntity.badRequest().build();
            }

            Path filePath = baseDir.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaTypeFactory.getMediaType(filePath.toString()).orElse(MediaType.APPLICATION_OCTET_STREAM))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
