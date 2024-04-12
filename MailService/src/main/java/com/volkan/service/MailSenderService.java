package com.volkan.service;


import com.volkan.rabbitmq.model.ResetPasswordModel;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailSenderService {
    private final JavaMailSender javaMailSender;

    public void sendNewPassword(ResetPasswordModel model) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("${spring.mail.username}");
        mailMessage.setTo(model.getEmail());
        mailMessage.setSubject("Password");

        mailMessage.setText( "Please use link provided, enter your email and temporary password below" +
                "\n"+
                "Link :http://localhost:9595/swagger-ui/index.html#/auth-controller/resetPassword" +
                "\n"+
                "Temporary password : " + model.getPassword());
        javaMailSender.send(mailMessage);
    }
}
