package com.example.service;

import com.example.dto.TransactionDTO;
import com.example.entity.Transaction;
import com.example.entity.UserEntity;
import com.example.repository.TransactionRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void saveTransaction(TransactionDTO transactionDTO) {
        UserEntity user = userRepository.findByUserId(transactionDTO.getUserId());
        Transaction transaction = createTransactionEntity(transactionDTO, user);
        this.transactionRepository.save(transaction);
    }

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


    @Override
    public TransactionDTO getTransactionDetails(Long transactionId) {
        return null;
    }

    private static final String API_URL = "https://api.fxratesapi.com/latest";


//    public void convertAndUpdateAmounts(TransactionDTO transaction) {
//        RestTemplate restTemplate = new RestTemplate();
//        String apiResponse = restTemplate.getForObject(API_URL, String.class);
//
//        double inrToUsdRate = extractConversionRate(apiResponse, "INR", "USD");
//        double usdToInrRate = extractConversionRate(apiResponse, "USD", "INR");
//
//        if (transaction.getAmountInr() != null) {
//            BigDecimal amountInr = transaction.getAmountInr();
//            double amountInrDouble = amountInr.doubleValue();
//            double amountUsd = inrToUsdRate * amountInrDouble;
//
//            // Set the amount_usd with the result
//            transaction.setAmountUsd(BigDecimal.valueOf(amountUsd));
//        }
//
//        updateAmounts(transaction, inrToUsdRate, usdToInrRate);
//
//        // Print the updated transaction details
//        System.out.println("Updated Transaction: " + transaction);
//    }
//
//    private double extractConversionRate(String apiResponse, String fromCurrency, String toCurrency) {
//        // Placeholder method - You need to implement this based on your actual response structure
//        // For simplicity, let's assume the response is in JSON format and you are using a JSON library
//        // to parse the response. Replace this with your actual logic.
//        // Example:
//        // return extractedConversionRate(apiResponse, fromCurrency, toCurrency);
//        return 0.0;
//    }
//
//    private void updateAmounts(Transaction transaction, double inrToUsdRate, double usdToInrRate) {
//        // Update the amounts based on the conversion rates
//        transaction.setAmountInr(transaction.getAmountInr() * inrToUsdRate);
//        transaction.setAmountUsd(transaction.getAmountUsd() * usdToInrRate);
//    }
}
