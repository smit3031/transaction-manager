package com.example.model;

import com.example.entity.TransactionSummary;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class DailyReport {
    private List<TransactionSummary> transactions;
    private BigDecimal totalAmountInINR;
    private BigDecimal totalAmountInUSD;
    private BigDecimal totalUsdPayments;
    private BigDecimal totalInrPayments;
    private BigDecimal totalCredit;
    private BigDecimal totalDebit;
}
