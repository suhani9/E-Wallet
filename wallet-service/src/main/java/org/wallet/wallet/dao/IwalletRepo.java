package org.wallet.wallet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wallet.wallet.entity.Wallet;

public interface IwalletRepo extends JpaRepository<Wallet, Long> {
    public Wallet findByUserId(long userId);
}
