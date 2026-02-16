package com.example.eventmanagement.events.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendTicketEmail(String toEmail,
                                String eventName,
                                String eventDate,
                                String eventAddress) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("🎟 Your Event Ticket - " + eventName);

        message.setText(
                "Your Booking is Confirmed!\n\n" +
                "Event: " + eventName + "\n" +
                "Date: " + eventDate + "\n" +
                "Location: " + eventAddress + "\n\n" +
                "Please show this email at entry.\n\n" +
                "Thank you for booking with EventHub!"
        );

        mailSender.send(message);
    }
}
