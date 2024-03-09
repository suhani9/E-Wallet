package org.wallet.wallet.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wallet.common.TransInitPayload;
import org.wallet.common.TransResponsePayload;
import org.wallet.common.UserCreatedPayload;
import org.wallet.common.WalletUpdatePayload;
import org.wallet.wallet.config.KafkaConsumerConfig;
import org.wallet.wallet.dao.IwalletRepo;
import org.wallet.wallet.entity.Wallet;

@Service
public class WalletService {
    private Logger logger = LoggerFactory.getLogger(WalletService.class);
    @Autowired
    private IwalletRepo walletRepo;

    @Value("${kafka.walletupdate.topic}")
    private String walletUpdatetopic;

    @Value("${kafka.transComplete.topic}")
    private String transCompletetopic;


    @Autowired
    private KafkaTemplate<String,Object> objectKafkaTemplate;

    @Transactional
    public void createNewUserWallet(UserCreatedPayload userCreatedPayLoad){
        Wallet newWallet = new Wallet();
        newWallet.setUserId(userCreatedPayLoad.getUserId());
        newWallet.setBalance(100.0);
        newWallet.setEmail(userCreatedPayLoad.getEmail());
        newWallet.setUserName(userCreatedPayLoad.getUserName());
        walletRepo.save(newWallet);
        WalletUpdatePayload kafkaWalletPayload = new WalletUpdatePayload(userCreatedPayLoad.getUserId(),userCreatedPayLoad.getUserName(),userCreatedPayLoad.getEmail(),100.0,100.0,"CREDIT");
        objectKafkaTemplate.send(walletUpdatetopic, String.valueOf(newWallet.getUserId()),kafkaWalletPayload);
        logger.info("pushed to kafka in topic {} : payload {}",walletUpdatetopic,kafkaWalletPayload);
        logger.info("Wallet created successfully for the user {}",userCreatedPayLoad.getUserId());
    }


    @Transactional
    public void doPayment(TransInitPayload transInitPayload) {
        Wallet senderWallet = walletRepo.findByUserId(transInitPayload.getFromUserId());
        Wallet receiverWallet = walletRepo.findByUserId(transInitPayload.getToUserId());
        TransResponsePayload responsePayload = new TransResponsePayload();
        responsePayload.setTrnxId(transInitPayload.getTrnxId());
        if(senderWallet.getBalance() >= transInitPayload.getAmt()){
            senderWallet.setBalance(senderWallet.getBalance()-transInitPayload.getAmt());
            receiverWallet.setBalance(receiverWallet.getBalance()+transInitPayload.getAmt());
            responsePayload.setSuccess(true);
            WalletUpdatePayload senderWalletPayload = new WalletUpdatePayload(senderWallet.getUserId(), senderWallet.getUserName(), senderWallet.getEmail(), senderWallet.getBalance(), transInitPayload.getAmt(),"DEBIT");
            objectKafkaTemplate.send(walletUpdatetopic, String.valueOf(senderWallet.getUserId()),senderWalletPayload);
            WalletUpdatePayload receiverWalletPayload = new WalletUpdatePayload(receiverWallet.getUserId(), receiverWallet.getUserName(), receiverWallet.getEmail(), receiverWallet.getBalance(), transInitPayload.getAmt(),"CREDIT");
            objectKafkaTemplate.send(walletUpdatetopic, String.valueOf(receiverWallet.getUserId()),receiverWalletPayload);
        }
        else{
            responsePayload.setSuccess(false);
            responsePayload.setReason("Insufficient Balance");
        }
        objectKafkaTemplate.send(transCompletetopic, String.valueOf(responsePayload.getTrnxId()),responsePayload);
        logger.info("pushed to kafka in topic {} : payload {}",transCompletetopic,responsePayload);
    }
}
