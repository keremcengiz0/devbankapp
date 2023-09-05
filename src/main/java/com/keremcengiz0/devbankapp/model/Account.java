package com.keremcengiz0.devbankapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Entity
public class Account {
    @Id
    private String id;

    private String customerId;
    private Double balance;
    private City city;
    private Currency currency;
}
