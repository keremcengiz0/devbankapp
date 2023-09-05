package com.keremcengiz0.devbankapp.dto;

import com.keremcengiz0.devbankapp.model.Currency;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class AccountDto implements Serializable {

    private String id;
    private String customerId;
    private Double balance;
    private Currency currency;
}
