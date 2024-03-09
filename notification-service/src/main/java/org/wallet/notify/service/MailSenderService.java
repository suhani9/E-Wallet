package org.wallet.notify.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.wallet.notify.entity.EmailRequest;

@Service
public class MailSenderService {



    @Autowired
    private JavaMailSender javaMailSender;

    public void  sendEmail(EmailRequest emailRequest){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("abhishekdahiya.dahiya48@gmail.com");
        simpleMailMessage.setSubject(emailRequest.getSubject());
        simpleMailMessage.setTo(emailRequest.getToEmail());
        simpleMailMessage.setText(emailRequest.getBody());
        //simpleMailMessage.setCc(emailRequest.getCc());
        javaMailSender.send(simpleMailMessage);


    }

}
