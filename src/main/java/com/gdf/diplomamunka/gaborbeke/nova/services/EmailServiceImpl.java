package com.gdf.diplomamunka.gaborbeke.nova.services;

import com.gdf.diplomamunka.gaborbeke.nova.email.Mail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;


@Service
public class EmailServiceImpl implements EmailService {

    private JavaMailSender mailSender;
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String senderEmail;


    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendEmail(Mail mail) throws MessagingException, IOException {


        final Context ctx = new Context(new Locale("HU-hu"));
        ctx.setVariable("subject", mail.getSubject());
        ctx.setVariable("username", mail.getUsername());
        ctx.setVariable("message", mail.getMessage());


        final String htmlContent = this.templateEngine.process("template", ctx);
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setFrom(new InternetAddress(senderEmail, "Nova alkalmazott", "UTF-8"));
        message.setTo(new InternetAddress(mail.getSendTo(), "Teszt Elek", "UTF-8"));
        message.setSubject(mail.getSubject());
        message.setText(htmlContent, true);
        this.mailSender.send(mimeMessage);
    }
}
