package org.wallet.trans.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.wallet.common.TransInitPayload;
import org.wallet.common.TransResponsePayload;
import org.wallet.trans.service.TransactionService;

@Configuration
public class KafkaTransConsumerConfig {

    private Logger logger = LoggerFactory.getLogger(KafkaTransConsumerConfig.class);
    @Autowired
    private TransactionService service;
    private ObjectMapper objectMapper = new ObjectMapper();
    @KafkaListener(topics = "TRNX-COMP",groupId = "trans-comp")
    public void consumerForTrnxInitTopic (ConsumerRecord payload) throws JsonProcessingException {
        TransResponsePayload transResponsePayload = objectMapper.readValue(payload.value().toString(),TransResponsePayload.class);
        logger.info("payload received to {}",transResponsePayload);
        service.updateTransaction(transResponsePayload);
    }
}
