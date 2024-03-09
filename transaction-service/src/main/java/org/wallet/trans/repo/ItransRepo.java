package org.wallet.trans.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wallet.trans.entity.Transaction;

public interface ItransRepo extends JpaRepository<Transaction, Long> {
    public Transaction findByTrxnId(String trnxId);
}
