package com.keremcengiz0.devbankapp.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoneyTransferRequest {
    private String fromId;
    private String toId;
    private Double amount;
}
