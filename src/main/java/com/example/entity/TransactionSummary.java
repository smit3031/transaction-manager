package com.example.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransactionSummary {
    String getTransactionId();
    BigDecimal getAmountInr();
    BigDecimal getAmountUsd();
    String getCurrency();
    LocalDateTime getTimestamp();
    Boolean getIsDebit();
    Boolean getIsCredit();
}
