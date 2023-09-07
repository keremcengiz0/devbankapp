package com.keremcengiz0.devbankapp.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoneyTransferRequest {
    private Long fromId;
    private Long toId;
    private Double amount;
}
