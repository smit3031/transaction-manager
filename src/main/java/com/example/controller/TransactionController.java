package com.example.controller;

import com.example.dto.TransactionDTO;
import com.example.exceptions.NoTransactionsFoundException;
import com.example.model.DailyReport;
import com.example.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Controller class for managing transactions and generating daily reports.
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    /**
     * Save a new transaction.
     *
     * @param transactionDTO The details of the transaction to be saved.
     * @return ResponseEntity with the saved TransactionDTO and HTTP status OK.
     */
    @PostMapping("/save")
    public ResponseEntity<TransactionDTO> saveTransaction(@RequestBody TransactionDTO transactionDTO) {
        TransactionDTO savedTransaction = transactionService.saveTransaction(transactionDTO);
        return ResponseEntity.ok(savedTransaction);
    }

    /**
     * Get the daily report for a specific user.
     *
     * @param userId The ID of the user for whom the daily report is requested.
     * @return ResponseEntity with the DailyReport and HTTP status OK if transactions are found.
     *         If no transactions are found, returns HTTP status NOT_FOUND.
     */
    @GetMapping("/dailyReport/{userId}")
    public ResponseEntity<DailyReport> getDailyReport(@PathVariable Long userId) {
        try {
            DailyReport dailyReport = transactionService.getDailyReport(userId);
            return new ResponseEntity<>(dailyReport, HttpStatus.OK);
        } catch (NoTransactionsFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

