package com.NgoServer.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.NgoServer.models.Certificate;
import com.NgoServer.models.Volunteer;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    private String cartificateBody = """
                        <!DOCTYPE html>
            <html>
            <head>
                <style>
                    .container {
                        font-family: Arial, sans-serif;
                        background-color: #f9f9f9;
                        padding: 30px;
                        border-radius: 10px;
                        width: 90%;
                        max-width: 600px;
                        margin: auto;
                        color: #333;
                    }
                    .header {
                        text-align: center;
                        background-color: #4CAF50;
                        color: white;
                        padding: 20px 0;
                        border-radius: 10px 10px 0 0;
                    }
                    .body {
                        padding: 20px;
                        background-color: white;
                    }
                    .footer {
                        margin-top: 30px;
                        text-align: center;
                        font-size: 12px;
                        color: #888;
                    }
                    .certificate {
                        display: block;
                        margin: 20px auto;
                        max-width: 100%;
                        border: 2px solid #ddd;
                        border-radius: 10px;
                    }
                    .btn {
                        display: inline-block;
                        margin-top: 20px;
                        background-color: #4CAF50;
                        color: white;
                        padding: 10px 20px;
                        text-decoration: none;
                        border-radius: 6px;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h2>🎉 Congratulations, [volunteerName]!</h2>
                    </div>
                    <div class="body">
                        <p>We are thrilled to recognize your valuable contribution as a volunteer.</p>
                        <p>Your dedication and hard work have made a real difference, and we're honored to present you with this certificate of achievement.</p>

                        <!-- Optional: Link or embedded image -->
                        <img class="certificate" src="[certificateUrl]" alt="Certificate of Achievement"/>

                        <p style="text-align:center; color:White;">
                            <a class="btn" href="[certificateUrl]" style="background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 6px;" target="_blank">Download Your Certificate</a>
                        </p>
                    </div>
                    <div class="footer">
                        This certificate is issued by [organizationName] | [date]
                    </div>
                </div>
            </body>
            </html>

                        """;;

    public void sendOTP(String email, int otp) {
        log.info("OTP: {}", otp);
        String subject = "Your OTP Code for Verification";
        String body = "Your OTP code is: " + otp
                + "\nPlease do not share this code with anyone. valid for 5 minutes, if not used will be expired.";

        try {
            // Create a simple email message
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject(subject);
            message.setText(body);

            // Send the email
            javaMailSender.send(message);
            log.info("OTP sent to email: {}", email);
        } catch (Exception e) {
            throw new RuntimeException("Error sending OTP email " + e.getMessage());
        }

    }

    public void sendEmail(String email, String subject, String body) {
        try {
            // Create a simple email message
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject(subject);
            message.setText(body);

            // Send the email
            javaMailSender.send(message);
            log.info("Email sent to: {}", email);
        } catch (Exception e) {
            log.error("Error sending email to: {}", email, e);
            throw new RuntimeException("Error sending email " + e.getMessage());
        }
    }

    /**
     * Send an email with the given certificate details to the given volunteer
     * 
     * @param volunteerEmail   the email address to send the email to
     * @param certificateImage the URL of the certificate image
     * @param certificate      the certificate details
     * @param volunteer        the volunteer details
     * 
     * @throws RuntimeException if there is an error sending the email
     */
    public void sendCertificateEmail(String volunteerEmail, String certificateImage, Certificate certificate,
            Volunteer volunteer) throws jakarta.mail.MessagingException {
        try {
            String subject = "Congratulations! Your Certificate is Ready!";
            // Prepare the email body using the template

            String body = cartificateBody
                    .replace("[volunteerName]", volunteer.getUser().getUsername())
                    .replace("[certificateUrl]", certificateImage)
                    .replace("[organizationName]", "NGO Server")
                    .replace("[date]", LocalDate.now().toString());

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(volunteerEmail);
            helper.setSubject(subject);
            helper.setText(body, true); // true = HTML content
            javaMailSender.send(message);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error sending email: " + e.getMessage());
        }
    }
}
