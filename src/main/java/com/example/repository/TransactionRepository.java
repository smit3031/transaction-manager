package com.example.repository;

import com.example.entity.Transaction;
import com.example.entity.TransactionSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Transaction} entities.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Retrieves a list of transactions for a specific user.
     *
     * @param userId The ID of the user.
     * @return List of transactions associated with the user.
     */
    @Query(value = "SELECT * FROM transaction WHERE user_id = :userId", nativeQuery = true)
    List<Transaction> findByUserId(@Param("userId") Long userId);

    /**
     * Retrieves a summary of transactions for a specific user on the current date.
     *
     * @param userId The ID of the user.
     * @return An optional list of TransactionSummary objects.
     */
    @Query(value = "SELECT transaction_id as transactionId, amount_inr as amountInr, " +
            "amount_usd as amountUsd, currency, timestamp, is_debit as isDebit, is_credit as isCredit " +
            "FROM transaction " +
            "WHERE user_id = :userId AND DATE(timestamp) = CURRENT_DATE", nativeQuery = true)
    Optional<List<TransactionSummary>> getTransactionByUserId(@Param("userId") Long userId);
}

