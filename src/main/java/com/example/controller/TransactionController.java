package com.example.controller;

import com.example.dto.TransactionDTO;
import com.example.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/save")
    public ResponseEntity<Void> saveTransaction(@RequestBody TransactionDTO transactionDTO) {
        // Business logic to save the transaction
        transactionService.saveTransaction(transactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Other methods for retrieving or manipulating transactions...
}

