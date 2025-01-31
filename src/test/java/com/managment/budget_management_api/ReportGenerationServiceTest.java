package com.managment.budget_management_api;

import com.managment.budget_management_api.Exceptions.UserNotFoundException;
import com.managment.budget_management_api.Model.Transaction;
import com.managment.budget_management_api.Model.TransactionSummary;
import com.managment.budget_management_api.Repository.TransactionRepository;
import com.managment.budget_management_api.Repository.UserRepository;
import com.managment.budget_management_api.Service.ReportGenerationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportGenerationServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReportGenerationService reportGenerationService;

    private LocalDate startDate;
    private LocalDate endDate;

    @BeforeEach
    void setUp() {
        startDate = LocalDate.of(2024, 1, 1);
        endDate = LocalDate.of(2024, 12, 31);
    }

    @Test
    void shouldCalculateIncomeVsExpensesSuccessfully() {
        when(userRepository.existsById(1)).thenReturn(true);

        List<Transaction> transactions = new ArrayList<>();

        Transaction incomeTransaction = new Transaction();
        incomeTransaction.setTransactionId(1);
        incomeTransaction.setTransactionType("INCOME");
        incomeTransaction.setAmount(BigDecimal.valueOf(1000));
        transactions.add(incomeTransaction);

        Transaction expenseTransaction = new Transaction();
        expenseTransaction.setTransactionId(2);
        expenseTransaction.setTransactionType("EXPENSE");
        expenseTransaction.setAmount(BigDecimal.valueOf(500));
        transactions.add(expenseTransaction);

        when(transactionRepository.findByUser_UserIdAndTransactionDateBetween(eq(1), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(transactions);

        TransactionSummary summary = reportGenerationService.calculateIncomeVsExpenses(1, startDate, endDate, null);

        assertNotNull(summary);
        assertEquals(BigDecimal.valueOf(1000), summary.getTotalIncome());
        assertEquals(BigDecimal.valueOf(500), summary.getTotalExpense());
        assertEquals(BigDecimal.valueOf(500), summary.getTotalBalance());

        verify(transactionRepository, times(1)).findByUser_UserIdAndTransactionDateBetween(eq(1), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void shouldThrowExceptionWhenNoTransactionsFound() {
        when(userRepository.existsById(1)).thenReturn(true);

        when(transactionRepository.findByUser_UserIdAndTransactionDateBetween(eq(1), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reportGenerationService.calculateIncomeVsExpenses(1, startDate, endDate, null);
        });

        assertEquals("No transactions found for the given filters.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Mock user does not exist
        when(userRepository.existsById(1)).thenReturn(false);

        // Expect exception
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            reportGenerationService.calculateIncomeVsExpenses(1, startDate, endDate, null);
        });

        assertEquals("User with Id: 1 not found", exception.getMessage());

        verify(transactionRepository, never()).findByUser_UserIdAndTransactionDateBetween(anyInt(), any(LocalDateTime.class), any(LocalDateTime.class));
    }
}
