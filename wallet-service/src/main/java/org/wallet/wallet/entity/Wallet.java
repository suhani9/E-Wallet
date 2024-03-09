package org.wallet.wallet.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Wallet {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long walletId;

    @Column
    private double balance;

    @Column(nullable = false,unique = true)
    private long userId;

    @Column(nullable = false,unique = true)
    private String email;

    @Column
    private String userName;
}
