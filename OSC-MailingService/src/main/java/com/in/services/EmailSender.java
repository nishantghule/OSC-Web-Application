package com.in.services;

import com.in.configs.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender javaMailSender;

    // Send Registration OTP email
    public void sendRegistrationEmail(String toEmail, String userId, String otpValue) {
        String subject = "OTP for Registration";
        String text = "Dear User, \n\nYour OTP for registration is: " + otpValue +
                "\n\nTo complete your registration, please visit the registration page and enter this OTP along with your User ID.\n\n\n" +
                "UserId: " + userId+"\n\nBest regards,\n" +
                "The OSC Team";

        sendEmail(toEmail, subject, text);
    }

    // Send Password Reset OTP email
    public void sendResetPasswordEmail(String toEmail, String otpValue) {
        String subject = "OTP for Password Reset";
        String text = "Dear User, \n\nYour OTP for password reset is: " + otpValue +
                "\n\nTo reset your password, please visit the password reset page and enter this OTP."+"\n\nBest regards,\n" +
                "The OSC Team";

        sendEmail(toEmail, subject, text);
    }

    // Generic method to send email
    private void sendEmail(String toEmail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(AppConstants.USERNAME);
        message.setTo(toEmail);                     // To email address
        message.setSubject(subject);                // Email subject
        message.setText(text);                      // Email body text

        javaMailSender.send(message);               // Send the email
    }
}
