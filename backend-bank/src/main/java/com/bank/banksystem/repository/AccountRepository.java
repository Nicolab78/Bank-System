package com.bank.banksystem.repository;

import com.bank.banksystem.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByOwnerId(Long userId);
}
