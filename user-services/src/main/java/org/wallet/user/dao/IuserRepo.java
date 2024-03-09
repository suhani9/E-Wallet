package org.wallet.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wallet.user.entity.User;

@Repository
public interface IuserRepo extends JpaRepository<User, Long> {
}
