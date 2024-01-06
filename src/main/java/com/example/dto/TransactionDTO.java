package com.example.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionDTO {
    private Long userId;
    private String transactionId;
    private boolean isDebit;
    private boolean isCredit;
    private BigDecimal amount_inr;
    private BigDecimal amount_usd;
    private String currency;
    private String paymentMethod;
    private LocalDateTime timestamp;
}

