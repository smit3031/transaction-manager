package com.example.service;

import com.example.dto.TransactionDTO;
import com.example.entity.Transaction;
import com.example.entity.TransactionSummary;
import com.example.entity.UserEntity;
import com.example.exceptions.NoTransactionsFoundException;
import com.example.model.DailyReport;
import com.example.repository.TransactionRepository;
import com.example.repository.UserRepository;
import com.example.util.CurrencyConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of the {@link TransactionService} interface.
 */
@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CurrencyConverter currencyConverter;

    /**
     * Saves a new transaction.
     *
     * @param transactionDTO The details of the transaction to be saved.
     * @return The saved TransactionDTO.
     */
    @Override
    public TransactionDTO saveTransaction(TransactionDTO transactionDTO) {
        if (transactionDTO.getAmount_inr()==null && transactionDTO.getAmount_usd()==null){
            throw new IllegalArgumentException("Amount can't be null provide it in either usd or inr");
        }
        UserEntity user = userRepository.findByUserId(transactionDTO.getUserId());
        setCurrencyData(transactionDTO);
        log.info("Amount in INR : {} and in USD : {} ", transactionDTO.getAmount_inr(), transactionDTO.getAmount_usd());
        Transaction transaction = createTransactionEntity(transactionDTO, user);
        transactionRepository.save(transaction);
        return transactionDTO;
    }

    /**
     * Retrieves the daily report for a specific user.
     *
     * @param userId The ID of the user for whom the daily report is requested.
     * @return The DailyReport.
     * @throws NoTransactionsFoundException If no transactions are found for the user on the current date.
     */
    @Override
    public DailyReport getDailyReport(Long userId) {
        DailyReport dailyReport = new DailyReport();
        Optional<List<TransactionSummary>> transactionSummaryList = transactionRepository.getTransactionByUserId(userId);
        if (transactionSummaryList.isPresent()){
            List<TransactionSummary> transactions = transactionSummaryList.get();
            log.info("Inside method found {} transactions", transactions.size());
            dailyReport.setTransactions(transactions);
            calculateTotals(dailyReport, transactions);
        }else{
            throw new NoTransactionsFoundException("No transactions found on the current date.");
        }

        return dailyReport;
    }

    /**
     * Calculates various totals for the provided transactions and updates the DailyReport.
     *
     * @param dailyReport   The DailyReport to be updated.
     * @param transactions  The list of transactions.
     */
    public void calculateTotals(DailyReport dailyReport, List<TransactionSummary> transactions) {
        log.info("Inside calculate data");

        BigDecimal totalINRPayments = transactions.stream()
                .filter(transaction -> Objects.nonNull(transaction.getAmountInr()) && "INR".equals(transaction.getCurrency()))
                .map(TransactionSummary::getAmountInr)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dailyReport.setTotalInrPayments(totalINRPayments);
        log.info("amountInr is {}", totalINRPayments);

        BigDecimal totalUSDPayments = transactions.stream()
                .filter(transaction -> Objects.nonNull(transaction.getAmountUsd()) && "USD".equals(transaction.getCurrency()))
                .map(TransactionSummary::getAmountUsd)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dailyReport.setTotalUsdPayments(totalUSDPayments);
        log.info("amountusd is {}", totalUSDPayments);

        BigDecimal totalAmountInr = transactions.stream()
                .filter(transaction -> Objects.nonNull(transaction.getAmountInr()))
                .map(TransactionSummary::getAmountInr)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dailyReport.setTotalAmountInINR(totalAmountInr);
        log.info("amount is {}", totalAmountInr);

        BigDecimal totalAmountUsd = transactions.stream()
                .filter(transaction -> Objects.nonNull(transaction.getAmountUsd()))
                .map(TransactionSummary::getAmountUsd)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dailyReport.setTotalAmountInUSD(totalAmountUsd);
        log.info("amount is {}", totalAmountUsd);



        BigDecimal totalCredit = transactions.stream()
                .filter(transaction -> Boolean.TRUE.equals(transaction.getIsCredit()) && Objects.nonNull(transaction.getAmountInr()))
                .map(TransactionSummary::getAmountInr)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dailyReport.setTotalCredit(totalCredit);
        log.info("credit is {}", totalCredit);

        BigDecimal totalDebit = transactions.stream()
                .filter(transaction -> Boolean.TRUE.equals(transaction.getIsDebit()) && Objects.nonNull(transaction.getAmountInr()))
                .map(TransactionSummary::getAmountInr)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dailyReport.setTotalDebit(totalDebit);
        log.info("debit is {}", totalDebit);

        log.info("got the data");
    }




    /**
     * Creates a new Transaction entity based on the provided TransactionDTO and UserEntity.
     *
     * @param transactionDTO The TransactionDTO containing transaction details.
     * @param userEntity     The UserEntity associated with the transaction.
     * @return The created Transaction entity.
     */
    private Transaction createTransactionEntity(TransactionDTO transactionDTO, UserEntity userEntity) {
        return Transaction.builder()
                .userEntity(userEntity)
                .transactionId(transactionDTO.getTransactionId())
                .isDebit(transactionDTO.isDebit())
                .isCredit(transactionDTO.isCredit())
                .amount_inr(transactionDTO.getAmount_inr())
                .amount_usd(transactionDTO.getAmount_usd())
                .currency(transactionDTO.getCurrency())
                .paymentMethod(transactionDTO.getPaymentMethod())
                .timestamp(transactionDTO.getTimestamp())
                .build();
    }

    /**
     * Sets the currency data in the TransactionDTO by converting amounts if necessary.
     *
     * @param transactionDTO The TransactionDTO to be updated.
     */
    public void setCurrencyData(TransactionDTO transactionDTO){
        if ("INR".equals(transactionDTO.getCurrency())){
            BigDecimal amount = transactionDTO.getAmount_inr();
            BigDecimal usd = currencyConverter.convert(amount, "INR", "USD");
            transactionDTO.setAmount_usd(usd);
        } else if ("USD".equals(transactionDTO.getCurrency())) {
            log.info("usd amount giben");
            BigDecimal amount = transactionDTO.getAmount_usd();
            BigDecimal inr = currencyConverter.convert(amount, "USD", "INR");
            transactionDTO.setAmount_inr(inr);
            log.info("inr amount is : {}", inr);
        } else{
            throw new IllegalArgumentException("Currency type not supported!");
        }
    }
}
