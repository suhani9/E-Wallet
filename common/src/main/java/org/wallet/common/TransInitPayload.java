package org.wallet.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransInitPayload {

    private String trnxId;
    private long fromUserId;
    private long toUserId;
    private double amt;

}
