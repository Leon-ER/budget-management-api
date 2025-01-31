package com.managment.budget_management_api;

import com.managment.budget_management_api.Exceptions.TransactionNotFoundException;
import com.managment.budget_management_api.Exceptions.UserNotFoundException;
import com.managment.budget_management_api.Model.Transaction;
import com.managment.budget_management_api.Model.TransactionSummary;
import com.managment.budget_management_api.Repository.TransactionRepository;
import com.managment.budget_management_api.Repository.UserRepository;
import com.managment.budget_management_api.Service.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Transaction testTransaction;

    @BeforeEach
    void setUp() {
        testTransaction = new Transaction();
        testTransaction.setTransactionId(1);
        testTransaction.setTransactionType("INCOME");
        testTransaction.setAmount(BigDecimal.valueOf(100.00));
        testTransaction.setDescription("Test transaction");
    }

    @Test
    void shouldSaveTransactionSuccessfully() {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);

        Transaction savedTransaction = transactionService.save(testTransaction);

        assertNotNull(savedTransaction);
        assertEquals("INCOME", savedTransaction.getTransactionType());
        verify(transactionRepository, times(1)).save(testTransaction);
    }

    @Test
    void shouldThrowExceptionWhenSavingNullTransaction() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.save(null);
        });

        assertEquals("Transaction cannot be null", exception.getMessage());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void shouldUpdateExistingTransactionSuccessfully() {
        when(transactionRepository.findById(1)).thenReturn(Optional.of(testTransaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);

        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setTransactionType("EXPENSE");
        updatedTransaction.setAmount(BigDecimal.valueOf(50.00));

        Transaction result = transactionService.update(1, updatedTransaction);

        assertEquals("EXPENSE", result.getTransactionType());
        assertEquals(BigDecimal.valueOf(50.00), result.getAmount());
        verify(transactionRepository, times(1)).findById(1);
        verify(transactionRepository, times(1)).save(testTransaction);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentTransaction() {
        when(transactionRepository.findById(1)).thenReturn(Optional.empty());

        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setTransactionType("EXPENSE");

        TransactionNotFoundException exception = assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.update(1, updatedTransaction);
        });

        assertEquals("Transaction with ID: 1 not found", exception.getMessage());
    }

    @Test
    void shouldFindTransactionByIdSuccessfully() {
        when(transactionRepository.findById(1)).thenReturn(Optional.of(testTransaction));

        Optional<Transaction> foundTransaction = transactionService.findById(1);

        assertTrue(foundTransaction.isPresent());
        assertEquals("INCOME", foundTransaction.get().getTransactionType());
    }

    @Test
    void shouldReturnEmptyOptionalWhenTransactionNotFound() {
        when(transactionRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Transaction> foundTransaction = transactionService.findById(1);

        assertTrue(foundTransaction.isEmpty());
    }

    @Test
    void shouldDeleteTransactionSuccessfully() {
        when(transactionRepository.findById(1)).thenReturn(Optional.of(testTransaction));
        doNothing().when(transactionRepository).delete(testTransaction);

        assertDoesNotThrow(() -> transactionService.deleteById(1));

        verify(transactionRepository, times(1)).delete(testTransaction);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentTransaction() {
        when(transactionRepository.findById(1)).thenReturn(Optional.empty());

        TransactionNotFoundException exception = assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.deleteById(1);
        });

        assertEquals("Transaction with ID: 1 not found", exception.getMessage());
        verify(transactionRepository, never()).delete(any());
    }

    @Test
    void shouldRetrieveTransactionSummarySuccessfully() {
        when(userRepository.existsById(1)).thenReturn(true);

        List<Transaction> transactions = new ArrayList<>();

        Transaction incomeTransaction = new Transaction();
        incomeTransaction.setTransactionId(1);
        incomeTransaction.setTransactionType("INCOME");
        incomeTransaction.setAmount(BigDecimal.valueOf(100));
        incomeTransaction.setDescription("Salary");
        transactions.add(incomeTransaction);

        Transaction expenseTransaction = new Transaction();
        expenseTransaction.setTransactionId(2);
        expenseTransaction.setTransactionType("EXPENSE");
        expenseTransaction.setAmount(BigDecimal.valueOf(50));
        expenseTransaction.setDescription("Groceries");
        transactions.add(expenseTransaction);

        when(transactionRepository.findByUser_UserId(1)).thenReturn((ArrayList<Transaction>) transactions);

        // Call the service method
        Optional<TransactionSummary> summary = transactionService.getSummary(1);

        // Assertions
        assertTrue(summary.isPresent());
        assertEquals(BigDecimal.valueOf(100), summary.get().getTotalIncome());
        assertEquals(BigDecimal.valueOf(50), summary.get().getTotalExpense());
        assertEquals(BigDecimal.valueOf(50), summary.get().getTotalBalance());
    }


    @Test
    void shouldThrowExceptionWhenRetrievingSummaryForNonExistentUser() {
        when(userRepository.existsById(1)).thenReturn(false);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            transactionService.getSummary(1);
        });

        assertEquals("User with ID: 1 not found", exception.getMessage());
    }
}
