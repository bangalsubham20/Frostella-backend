package com.frostella.backend.controller;
import com.frostella.backend.model.Order;
import com.frostella.backend.model.OrderStatus;
import com.frostella.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private com.frostella.backend.service.EmailService emailService;

    @GetMapping
    public List<Order> getAll() { return orderRepository.findAll(); }

    @PostMapping
    public Order create(@RequestBody Order order) {
        Order savedOrder = orderRepository.save(order);
        emailService.sendOrderConfirmation(savedOrder);
        return savedOrder;
    }

    @GetMapping("/user/{userId}")
    public List<Order> getByUser(@PathVariable Long userId) { return orderRepository.findByUserId(userId); }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody java.util.Map<String, String> payload) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) return ResponseEntity.notFound().build();
        
        try {
            order.setStatus(OrderStatus.valueOf(payload.get("status").toUpperCase()));
            return ResponseEntity.ok(orderRepository.save(order));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status");
        }
    }
}