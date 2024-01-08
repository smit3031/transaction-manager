package com.example.service;

import com.example.dto.TransactionDTO;
import com.example.entity.UserEntity;
import com.example.exceptions.NoTransactionsFoundException;
import com.example.model.DailyReport;
import com.example.repository.TransactionRepository;
import com.example.repository.UserRepository;
import com.example.util.CurrencyConverter;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class TransactionServiceImplTests {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrencyConverter currencyConverter;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void saveTransaction_WithValidTransactionDTO_CallsRepositorySave() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setUserId(1L);
        transactionDTO.setAmount_inr(new BigDecimal("100.00"));
        transactionDTO.setCurrency("INR");
        UserEntity userEntity = new UserEntity();
        when(userRepository.findByUserId(transactionDTO.getUserId())).thenReturn(userEntity);
        when(currencyConverter.convert(any(), any(), any())).thenReturn(BigDecimal.ONE);

        transactionService.saveTransaction(transactionDTO);

        verify(transactionRepository, times(1)).save(any());
    }

    @Test
    void getDailyReport_WithValidUserId_ReturnsDailyReport() {
        Long userId = 1L;
        when(transactionRepository.getTransactionByUserId(userId)).thenReturn(Optional.of(Collections.emptyList()));

        DailyReport dailyReport = transactionService.getDailyReport(userId);

        // Add assertions based on the expected behavior
    }

    @Test
    void getDailyReport_WithInvalidUserId_ThrowsNoTransactionsFoundException() {
        Long userId = 1L;
        when(transactionRepository.getTransactionByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(NoTransactionsFoundException.class, () -> transactionService.getDailyReport(userId));
    }
}

