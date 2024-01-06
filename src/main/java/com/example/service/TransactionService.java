package com.example.service;

import com.example.dto.TransactionDTO;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {
    void saveTransaction(TransactionDTO transactionDTO);
    TransactionDTO getTransactionDetails(Long transactionId);
}
