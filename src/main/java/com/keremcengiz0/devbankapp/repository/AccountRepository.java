package com.keremcengiz0.devbankapp.repository;

import com.keremcengiz0.devbankapp.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
