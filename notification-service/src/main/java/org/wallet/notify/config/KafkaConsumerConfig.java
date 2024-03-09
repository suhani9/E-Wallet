package org.wallet.notify.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.wallet.common.UserCreatedPayload;
import org.wallet.common.WalletUpdatePayload;
import org.wallet.notify.entity.EmailRequest;
import org.wallet.notify.service.MailSenderService;

@Configuration
public class KafkaConsumerConfig {
    Logger logger = LoggerFactory.getLogger(KafkaConsumerConfig.class);
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MailSenderService mailSenderService;
    @KafkaListener(topics = "USER_CREATED",groupId = "email-sender")
    private void consumerForUserCreatedTopic(ConsumerRecord payload) throws JsonProcessingException {
        UserCreatedPayload userCreatedPayload = mapper.readValue(payload.value().toString(),UserCreatedPayload.class);
        logger.info("received the payload for email sender service {}", userCreatedPayload);
        String body = "Hi " +userCreatedPayload.getUserName() + " ,E-Wallet Welcomes you";
        EmailRequest emailRequest = new EmailRequest(userCreatedPayload.getEmail(),"Welocme to E-Wallet",body,"");
        mailSenderService.sendEmail(emailRequest);
        logger.info("Welcome email sent to user {}",userCreatedPayload.getUserId());
    }

    @KafkaListener(topics = "WALLET-UPDATE",groupId = "balance-update-notifier")
    private void consumerForWalletUpdateTopic(ConsumerRecord payload) throws JsonProcessingException {
        WalletUpdatePayload walletUpdatePayload = mapper.readValue(payload.value().toString(),WalletUpdatePayload.class);
        logger.info("received the payload for email sender service {}", walletUpdatePayload);
        String body = String.format("Hi %s, %s amount is %s in your account .Your current balance is %s",walletUpdatePayload.getUserName(),walletUpdatePayload.getAmt(),walletUpdatePayload.getAction(),walletUpdatePayload.getBalance());
        EmailRequest emailRequest = new EmailRequest(walletUpdatePayload.getEmail(),"Balance Summary from E-Wallet",body,"");
        mailSenderService.sendEmail(emailRequest);
        logger.info("balance email sent to user {}",walletUpdatePayload.getUserId());
    }

}
