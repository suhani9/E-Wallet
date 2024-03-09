package org.wallet.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransResponsePayload {
    private String trnxId;
    private boolean success;
    private String reason;
}
