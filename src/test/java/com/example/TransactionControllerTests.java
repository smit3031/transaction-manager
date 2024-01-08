package com.example;

import com.example.controller.TransactionController;
import com.example.dto.TransactionDTO;
import com.example.exceptions.NoTransactionsFoundException;
import com.example.model.DailyReport;
import com.example.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class TransactionControllerTests {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @Test
    void saveTransaction_ReturnsOkResponse() {
        TransactionDTO transactionDTO = new TransactionDTO();
        when(transactionService.saveTransaction(transactionDTO)).thenReturn(transactionDTO);

        ResponseEntity<TransactionDTO> response = transactionController.saveTransaction(transactionDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactionDTO, response.getBody());
    }

    @Test
    void getDailyReport_WithValidUserId_ReturnsOkResponse() {
        Long userId = 1L;
        DailyReport dailyReport = new DailyReport();
        when(transactionService.getDailyReport(userId)).thenReturn(dailyReport);

        ResponseEntity<DailyReport> response = transactionController.getDailyReport(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dailyReport, response.getBody());
    }

    @Test
    void getDailyReport_WithInvalidUserId_ReturnsNotFoundResponse() {
        Long userId = 1L;
        when(transactionService.getDailyReport(userId)).thenThrow(new NoTransactionsFoundException("No transactions found."));

        ResponseEntity<DailyReport> response = transactionController.getDailyReport(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}

