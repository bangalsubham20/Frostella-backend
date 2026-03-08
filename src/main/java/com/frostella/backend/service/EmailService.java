package com.frostella.backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
        if ("mock_bakery_email@gmail.com".equals(fromEmail)) {
            System.out.println("------------------------------------------");
            System.out.println("MOCK PREMIUM EMAIL SERVICE");
            System.out.println("Recipient: " + order.getUser().getEmail());
            System.out.println("Subject: Frostella Reservation #" + order.getId());
            System.out.println("Body: [Gourmet HTML Template Generated]");
            System.out.println("------------------------------------------");
            return;
        }

        try {
            sendHtmlEmail(order);
        } catch (MessagingException e) {
            System.err.println("Critical failure in Premium Email Delivery: " + e.getMessage());
        }
    }

    private void sendHtmlEmail(Order order) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String htmlMsg = "<div style=\"font-family: 'Playfair Display', serif; max-width: 600px; margin: auto; border: 1px solid #f48fb1; padding: 40px; border-radius: 40px;\">" +
                "<h1 style=\"color: #2D3436; text-align: center; font-style: italic;\">Frostella</h1>" +
                "<p style=\"color: #F48FB1; text-align: center; font-weight: bold; text-transform: uppercase; letter-spacing: 4px; font-size: 10px;\">The Art of Baking</p>" +
                "<hr style=\"border: 0; border-top: 1px solid #eee; margin: 30px 0;\">" +
                "<h2 style=\"color: #2D3436; text-align: center;\">Gourmet Reservation Secured</h2>" +
                "<p style=\"color: #636E72; line-height: 1.6; text-align: center;\">Hello " + order.getUser().getName() + ", your artisanal treats are being prepared.</p>" +
                "<div style=\"background: #fafafa; padding: 25px; border-radius: 20px; margin: 30px 0;\">" +
                "<p style=\"margin: 0; font-size: 12px; color: #b2bec3; font-weight: bold; text-transform: uppercase;\">Order Identifier</p>" +
                "<p style=\"margin: 5px 0 20px 0; font-size: 24px; color: #F48FB1; font-weight: bold;\">#" + order.getId() + "</p>" +
                "<p style=\"margin: 0; font-size: 12px; color: #b2bec3; font-weight: bold; text-transform: uppercase;\">Total Investment</p>" +
                "<p style=\"margin: 5px 0 0 0; font-size: 24px; color: #2D3436; font-weight: bold;\">₹" + order.getTotalAmount() + "</p>" +
                "</div>" +
                "<p style=\"color: #636E72; text-align: center; font-style: italic;\">\"Handcrafted in Rudrapur with love and precision.\"</p>" +
                "</div>";

        helper.setText(htmlMsg, true);
        helper.setTo(order.getUser().getEmail());
        helper.setSubject("Your Frostella Reservation Details - #" + order.getId());
        helper.setFrom(fromEmail);

        mailSender.send(mimeMessage);
    }
}
