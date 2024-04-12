package com.volkan.rabbitmq.consumer;

import com.volkan.rabbitmq.model.ResetPasswordModel;
import com.volkan.service.MailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetPasswordConsumer {
    private final MailSenderService mailSenderService;

    @RabbitListener(queues = "reset-password-queue")
    public void sendNewPassword(ResetPasswordModel model){
        mailSenderService.sendNewPassword(model);
    }

}
