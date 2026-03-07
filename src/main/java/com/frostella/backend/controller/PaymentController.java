package com.frostella.backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> data) {
        try {
            // Amount is expected in basic paise/cents (handled seamlessly from React frontend)
            int amount = (int) (data.get("amount"));
            
            // Initialize the authentic Razorpay client instance
            RazorpayClient razorpayClient = new RazorpayClient(keyId, keySecret);
            
            // Format order structure for Razorpay servers
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "txn_" + System.currentTimeMillis());
            
            // Generate official Test Order from Razorpay
            Order order = razorpayClient.orders.create(orderRequest);
            
            Map<String, Object> response = new HashMap<>();
            response.put("orderId", order.get("id"));
            response.put("amount", order.get("amount"));
            response.put("currency", order.get("currency"));
            return ResponseEntity.ok(response);
            
        } catch (RazorpayException e) {
            return ResponseEntity.internalServerError().body("Razorpay API Request failed: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Payment server error: " + e.getMessage());
        }
    }
}
