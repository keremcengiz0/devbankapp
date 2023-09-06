package com.keremcengiz0.devbankapp.repository;

import com.keremcengiz0.devbankapp.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
