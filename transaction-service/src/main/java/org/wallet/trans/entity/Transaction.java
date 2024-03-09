package org.wallet.trans.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wallet.trans.enums.TransactionStatus;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String trxnId;

    @Column(nullable = false)
    private long fromUserId;

    @Column(nullable = false)
    private long toUserId;

    @Column(nullable = false)
    private double amt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column
    private String reason;
}
