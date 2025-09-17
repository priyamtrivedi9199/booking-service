package com.otelier.backend.service;

import com.otelier.backend.model.Booking;
import com.sendgrid.Mail;
import com.sendgrid.SendGrid;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    
    @Value("${SENDGRID_API_KEY:}")
    private String sendGridApiKey;
    
    @Value("${notification.support-email}")
    private String supportEmail;
    
    @Value("${notification.from-email}")
    private String fromEmail;
    
    public void notifyBookingCreated(Booking booking) {
        if (sendGridApiKey.isEmpty()) {
            logger.warn("SendGrid API key not configured, skipping email notification");
            return;
        }
        
        try {
            Email from = new Email(fromEmail);
            Email to = new Email(supportEmail);
            String subject = "New Booking Created - Hotel " + booking.getHotelId();
            
            String emailContent = buildEmailContent(booking);
            Content content = new Content("text/html", emailContent);
            
            Mail mail = new Mail(from, subject, to, content);
            
            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            
            Response response = sg.api(request);
            logger.info("Email notification sent for booking: {}, status: {}", 
                       booking.getId(), response.getStatusCode());
            
        } catch (Exception e) {
            logger.error("Failed to send email notification for booking: {}", booking.getId(), e);
            throw new RuntimeException("Email notification failed", e);
        }
    }
    
    private String buildEmailContent(Booking booking) {
        return String.format("""
            <h2>New Booking Created</h2>
            <p><strong>Booking ID:</strong> %s</p>
            <p><strong>Hotel ID:</strong> %s</p>
            <p><strong>Guest:</strong> %s (%s)</p>
            <p><strong>Room:</strong> %s</p>
            <p><strong>Check-in:</strong> %s</p>
            <p><strong>Check-out:</strong> %s</p>
            <p><strong>Total Amount:</strong> $%s</p>
            <p><strong>Created At:</strong> %s</p>
            """,
            booking.getId(),
            booking.getHotelId(),
            booking.getGuestName(),
            booking.getGuestEmail(),
            booking.getRoomNumber(),
            booking.getCheckInDate(),
            booking.getCheckOutDate(),
            booking.getTotalAmount(),
            booking.getCreatedAt()
        );
    }
}
