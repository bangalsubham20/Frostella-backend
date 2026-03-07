package com.frostella.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import com.frostella.backend.model.Order;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOrderConfirmation(Order order) {
        // Safe mock bypass so application doesn't freeze on start/mock orders
        if ("mock_bakery_email@gmail.com".equals(fromEmail)) {
            System.out.println("MOCK EMAIL SYSTEM: Sent Order #" + order.getId() + " receipt to Customer!");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            
            // Assume we had a string accessor for customer email, 
            // but for simplicity, let's suppose the `User` has `email`
            message.setTo(order.getUser().getEmail());
            message.setSubject("Frostella Bakery - Order #" + order.getId() + " Placed!");
            message.setText("Hello " + order.getUser().getName() + ",\n\n" +
                    "Thank you for choosing Frostella for your sweet moments! 🎂\n\n" +
                    "We have successfully received your order.\n" +
                    "Total Amount: ₹" + order.getTotalAmount() + "\n" +
                    "Date: " + order.getOrderDate() + "\n\n" +
                    "Stay sweet!\n" +
                    "Frostella Bakery Team");

            mailSender.send(message);

            // Notify Admin as well
            SimpleMailMessage adminMsg = new SimpleMailMessage();
            adminMsg.setFrom(fromEmail);
            adminMsg.setTo("admin@frostella.com"); // hardcoded Roshni's email ideally
            adminMsg.setSubject("New Frostella Order Received - #" + order.getId());
            adminMsg.setText("A new order was placed by " + order.getUser().getName() + " for ₹" + order.getTotalAmount());
            mailSender.send(adminMsg);

        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
