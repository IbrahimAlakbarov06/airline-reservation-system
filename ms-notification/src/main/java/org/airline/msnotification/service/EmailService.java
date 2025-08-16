package org.airline.msnotification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendWelcomeEmail(String email, String firstName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Welcome to Airline Booking!");
            message.setText(String.format("""
                    Hello %s !
                
                Welcome to our Airline Booking System!
                
                Your account has been created successfully.
                You can now search and book flights.
                
                Next steps:
                - Complete your profile
                - Search for flights
                - Book your first trip!
                
                Happy travels!
                Airline Team
                """, firstName));

            mailSender.send(message);
            log.info("Email sent successfully to " + email);
        } catch (Exception e) {
            log.error("Failed to send welcome to email: {}"+email, e.getMessage());
        }
    }

    public void sendProfileCreatedEmail(String email, String firstName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Profile Created!");
            message.setText(String.format("""
                Hello %s!
                
                Your travel profile has been created!
                
                Complete your profile with:
                - Passport details
                - Date of birth
                - Nationality
                
                This will make booking faster!
                
                Airline Team
                """, firstName));

            mailSender.send(message);
            log.info("Profile created email sent to: {}", email);

        } catch (Exception e) {
            log.error("Failed to send profile created email to: {}", email, e);
        }
    }

    public void sendProfileCompletedEmail(String email, String firstName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Profile Complete - Ready to Fly! ");
            message.setText(String.format("""
                Hello %s!
                
                Congratulations! Your profile is now complete! 
                
                You can now:
                ✅ Book flights instantly
                ✅ Get age-based discounts
                ✅ Quick checkout process
                
                Start searching for flights now!
                
                Happy travels!
                Airline Team
                """, firstName));

            mailSender.send(message);
            log.info("Profile completed email sent to: {}", email);

        } catch (Exception e) {
            log.error("Failed to send profile completed email to: {}", email, e);
        }
    }
}
