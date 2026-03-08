package com.frostella.backend.service;

import com.frostella.backend.model.Order;
import com.frostella.backend.model.OrderItem;
import com.frostella.backend.model.OrderStatus;
import com.frostella.backend.model.User;
import com.frostella.backend.repository.OrderRepository;
import com.frostella.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public Order placeOrder(Order order) {
        order.setOrderDate(LocalDateTime.now());
        if (order.getStatus() == null) {
            order.setStatus(OrderStatus.PENDING);
        }
        
        // Link order items to order
        for (OrderItem item : order.getOrderItems()) {
            item.setOrder(order);
        }

        Order savedOrder = orderRepository.save(order);
        
        // Send confirmation email
        try {
            User user = userRepository.findById(order.getUser().getId()).orElse(null);
            if (user != null) {
                emailService.sendOrderConfirmation(savedOrder);
            }
        } catch (Exception e) {
            // Log error but don't fail the order
            System.err.println("Failed to send order confirmation email: " + e.getMessage());
        }

        return savedOrder;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public Order updateOrderStatus(Long id, OrderStatus status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
