package org.wallet.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletUpdatePayload {

    private long userId;
    private String userName;
    private String email;
    private double balance;
    private double amt;
    private String action;

}
