package org.wallet.trans.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionDto {

    @NotNull
    private long fromUserId;
    @NotNull
    private long toUserId;
    @NotNull
    private double amount;
}
