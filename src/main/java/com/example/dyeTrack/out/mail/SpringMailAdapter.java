package com.example.dyeTrack.out.mail;

import com.example.dyeTrack.core.port.out.MailPort;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


@Component
public class SpringMailAdapter implements MailPort {

    private final JavaMailSender mailSender;

    public SpringMailAdapter(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendExportMail(String to, byte[] fileBytes, String filename) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Your training sessions export is ready");

        helper.setText(
                """
                        Hello,
                            
                        Your training sessions export has been successfully generated.
         
                        You will find attached a file containing all your data for the selected period.
                        It includes:
                            • 1 sheet with visual session data for quick insights
                            • 2 structured data sheets that you can reuse for your own analysis or tools
                                
                        If you did not request this export, you can safely ignore this email.
                            
                        Thank you for using DyeTrack.
                        Stay consistent, stay strong 💪
                            
                        — The DyeTrack Team
                        """,
                false
        );

        helper.addAttachment(filename, new ByteArrayResource(fileBytes));

        mailSender.send(message);
    }
}