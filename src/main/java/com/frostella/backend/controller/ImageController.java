package com.frostella.backend.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private Cloudinary cloudinary;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            // MOCK OVERRIDE: Since we don't have real credentials right now,
            // we will simulate a successful cloudinary upload for demo purposes.
            // If the credentials start with "mock_", return a dummy robust URL.
            if ("mock_cloud_name".equals(cloudinary.config.cloudName)) {
                String mockUrl = "https://res.cloudinary.com/demo/image/upload/v1612345678/sample.jpg";
                Map<String, String> response = new HashMap<>();
                response.put("url", mockUrl);
                return ResponseEntity.ok(response);
            }

            // PRODUCTION IMPLEMENTATION: Uploads directly to Cloudinary
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            
            String imageUrl = uploadResult.get("secure_url").toString();
            
            Map<String, String> response = new HashMap<>();
            response.put("url", imageUrl);
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to upload image: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Cloudinary error: " + e.getMessage());
        }
    }
}
