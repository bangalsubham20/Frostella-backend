package com.frostella.backend.controller;

import com.frostella.backend.model.Review;
import com.frostella.backend.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/product/{productId}")
    public List<Review> getByProduct(@PathVariable Long productId) {
        return reviewService.getReviewsByProductId(productId);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public Review addReview(@RequestBody Review review) {
        return reviewService.addReview(review);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
