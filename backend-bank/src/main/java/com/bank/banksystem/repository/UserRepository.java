package com.bank.banksystem.repository;

import com.bank.banksystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByEnabled(boolean enabled);
    Optional<User> findByUsername(String username);
}