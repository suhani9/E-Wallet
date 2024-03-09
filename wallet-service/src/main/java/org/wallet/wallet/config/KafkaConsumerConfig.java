package org.wallet.wallet.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.wallet.common.TransInitPayload;
import org.wallet.common.UserCreatedPayload;
import org.wallet.wallet.service.WalletService;

@Configuration
public class KafkaConsumerConfig {

    private Logger logger = LoggerFactory.getLogger(KafkaConsumerConfig.class);
    @Autowired
    private WalletService service;


    private static ObjectMapper objectMapper = new ObjectMapper();


    @KafkaListener(topics = "USER_CREATED",groupId = "wallet-creation")
    public void consumerForUserCreatedTopic (ConsumerRecord payload) throws JsonProcessingException {
        UserCreatedPayload userCreatedPayLoad = objectMapper.readValue(payload.value().toString(),UserCreatedPayload.class);
        service.createNewUserWallet(userCreatedPayLoad);
    }
    @KafkaListener(topics = "TRNX-INIT",groupId = "wallet-update")
    public void consumerForTrnxInitTopic (ConsumerRecord payload) throws JsonProcessingException {
        TransInitPayload transInitPayload = objectMapper.readValue(payload.value().toString(),TransInitPayload.class);
        service.doPayment(transInitPayload);
    }

}
