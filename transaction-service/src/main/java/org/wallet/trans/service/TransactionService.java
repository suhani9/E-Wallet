package org.wallet.trans.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wallet.common.TransInitPayload;
import org.wallet.common.TransResponsePayload;
import org.wallet.trans.dto.TransactionDto;
import org.wallet.trans.entity.Transaction;
import org.wallet.trans.enums.TransactionStatus;
import org.wallet.trans.repo.ItransRepo;

import java.util.UUID;

@Service
public class TransactionService {

    private Logger logger = LoggerFactory.getLogger(TransactionService.class);
    @Autowired
    private ItransRepo itransRepo;

    @Autowired
    private KafkaTemplate<String,Object>objectKafkaTemplate;

    @Transactional
    public String makePayment(TransactionDto transactionDto){
        Transaction newtransaction = mapTransDtoToTrans(transactionDto);
        itransRepo.save(newtransaction);
        TransInitPayload transInitPayload = new TransInitPayload(newtransaction.getTrxnId(),newtransaction.getFromUserId(),newtransaction.getToUserId(),newtransaction.getAmt());
        objectKafkaTemplate.send("TRNX-INIT",newtransaction.getTrxnId(),transInitPayload);
        logger.info("pushed to kafka in topic TRNX-INIT : payload {}",transInitPayload);
        return newtransaction.getTrxnId();
    }

    private Transaction mapTransDtoToTrans(TransactionDto transactionDto) {

        Transaction transaction = new Transaction();
        transaction.setFromUserId(transactionDto.getFromUserId());
        transaction.setToUserId(transactionDto.getToUserId());
        transaction.setAmt(transactionDto.getAmount());
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setTrxnId(UUID.randomUUID().toString());
        return transaction;
    }

    @Transactional
    public void updateTransaction(TransResponsePayload transResponsePayload) {
        Transaction transaction = itransRepo.findByTrxnId(transResponsePayload.getTrnxId());
        if(transResponsePayload.isSuccess()){
            transaction.setStatus(TransactionStatus.COMPLETED);
        }
        else {
            transaction.setStatus(TransactionStatus.FAILED);
            transaction.setReason(transResponsePayload.getReason());
        }
    }

    public TransResponsePayload getPaymentStatus(String trnxId) {
        Transaction transaction = itransRepo.findByTrxnId(trnxId);
        TransResponsePayload responsePayload = new TransResponsePayload();
        responsePayload.setTrnxId(trnxId);
        responsePayload.setSuccess(transaction.getStatus().equals(TransactionStatus.COMPLETED));
        responsePayload.setReason(transaction.getReason());
        return responsePayload;
    }
}
