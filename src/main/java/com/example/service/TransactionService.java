package com.example.service;

import com.example.dto.TransactionDTO;
import com.example.entity.TransactionSummary;
import com.example.model.DailyReport;
import org.springframework.stereotype.Service;

/**
 * Service interface for managing transactions and generating reports.
 */
@Service
public interface TransactionService {

    /**
     * Save a new transaction.
     *
     * @param transactionDTO The details of the transaction to be saved.
     * @return The saved TransactionDTO.
     */
    TransactionDTO saveTransaction(TransactionDTO transactionDTO);

    /**
     * Get the daily report for a specific user.
     *
     * @param userId The ID of the user for whom the daily report is requested.
     * @return The DailyReport.
     */
    DailyReport getDailyReport(Long userId);

}
