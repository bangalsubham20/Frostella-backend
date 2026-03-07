package com.frostella.backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
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
            int amount = (int) (data.get("amount"));
            RazorpayClient razorpayClient = new RazorpayClient(keyId, keySecret);
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "txn_" + System.currentTimeMillis());
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

    @PostMapping("/verify-payment")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> data) {
        String razorpayOrderId = data.get("razorpay_order_id");
        String razorpayPaymentId = data.get("razorpay_payment_id");
        String razorpaySignature = data.get("razorpay_signature");

        try {
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", razorpayOrderId);
            options.put("razorpay_payment_id", razorpayPaymentId);
            options.put("razorpay_signature", razorpaySignature);

            boolean isValid = Utils.verifyPaymentSignature(options, keySecret);

            if (isValid) {
                return ResponseEntity.ok(Map.of("status", "success", "message", "Payment verified successfully"));
            } else {
                return ResponseEntity.status(400).body(Map.of("status", "failed", "message", "Payment verification failed"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Verification error: " + e.getMessage());
        }
    }
}
