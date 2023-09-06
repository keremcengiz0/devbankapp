package com.keremcengiz0.devbankapp.repository;

import com.keremcengiz0.devbankapp.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
