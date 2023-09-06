package com.keremcengiz0.devbankapp.repository;

import com.keremcengiz0.devbankapp.model.Cities;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CitiesRepository extends JpaRepository<Cities, Long> {
    Optional<Cities> findByPlateCode(Integer plateCode);
}
