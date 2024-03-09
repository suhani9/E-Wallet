package org.wallet.trans.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wallet.common.TransResponsePayload;
import org.wallet.trans.dto.TransactionDto;
import org.wallet.trans.service.TransactionService;

@RestController
@RequestMapping("/trans-service")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/sendMoney")
    public ResponseEntity<String> doPayment(@RequestBody @Valid TransactionDto transactionDto){
        String trxnId = transactionService.makePayment(transactionDto);
        return new ResponseEntity<>(trxnId, HttpStatus.ACCEPTED);
    }

    @GetMapping("/getStatus/{trnxId}")
    public ResponseEntity<TransResponsePayload> checkPaymentStatus(@PathVariable String trnxId){
        TransResponsePayload responsePayload = transactionService.getPaymentStatus(trnxId);
        return new ResponseEntity<>(responsePayload,HttpStatus.OK);
    }
}
