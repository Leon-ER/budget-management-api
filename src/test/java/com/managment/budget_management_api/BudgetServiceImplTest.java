package com.managment.budget_management_api;

import com.managment.budget_management_api.Exceptions.BudgetNotFoundException;
import com.managment.budget_management_api.Model.Budget;
import com.managment.budget_management_api.Repository.BudgetRepository;
import com.managment.budget_management_api.Service.BudgetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BudgetServiceImplTest {

    @Mock
    private BudgetRepository budgetRepository;

    @InjectMocks
    private BudgetServiceImpl budgetService;

    private Budget testBudget;

    @BeforeEach
    void setUp() {
        testBudget = new Budget();
        testBudget.setBudgetId(1);
        testBudget.setCategoryName("Groceries");
        testBudget.setTotalBudget(BigDecimal.valueOf(500));
    }

    @Test
    void shouldSaveBudgetSuccessfully() {
        when(budgetRepository.save(any(Budget.class))).thenReturn(testBudget);

        Budget savedBudget = budgetService.save(testBudget);

        assertNotNull(savedBudget);
        assertEquals("Groceries", savedBudget.getCategoryName());
        verify(budgetRepository, times(1)).save(testBudget);
    }

    @Test
    void shouldThrowExceptionWhenSavingNullBudget() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            budgetService.save(null);
        });

        assertEquals("Budget cannot be null", exception.getMessage());
        verify(budgetRepository, never()).save(any());
    }

    @Test
    void shouldUpdateExistingBudgetSuccessfully() {
        when(budgetRepository.findById(1)).thenReturn(Optional.of(testBudget));
        when(budgetRepository.save(any(Budget.class))).thenReturn(testBudget);

        Budget updatedBudget = new Budget();
        updatedBudget.setCategoryName("Travel");
        updatedBudget.setTotalBudget(BigDecimal.valueOf(1000));

        Budget result = budgetService.update(1, updatedBudget);

        assertEquals("Travel", result.getCategoryName());
        assertEquals(BigDecimal.valueOf(1000), result.getTotalBudget());
        verify(budgetRepository, times(1)).findById(1);
        verify(budgetRepository, times(1)).save(testBudget);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentBudget() {
        when(budgetRepository.findById(1)).thenReturn(Optional.empty());

        Budget updatedBudget = new Budget();
        updatedBudget.setCategoryName("Travel");

        BudgetNotFoundException exception = assertThrows(BudgetNotFoundException.class, () -> {
            budgetService.update(1, updatedBudget);
        });

        assertEquals("Budget with ID: 1 not found", exception.getMessage());
    }

    @Test
    void shouldFindBudgetByIdSuccessfully() {
        when(budgetRepository.findById(1)).thenReturn(Optional.of(testBudget));

        Optional<Budget> foundBudget = budgetService.findById(1);

        assertTrue(foundBudget.isPresent());
        assertEquals("Groceries", foundBudget.get().getCategoryName());
    }

    @Test
    void shouldReturnEmptyOptionalWhenBudgetNotFound() {
        when(budgetRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Budget> foundBudget = budgetService.findById(1);

        assertTrue(foundBudget.isEmpty());
    }

    @Test
    void shouldDeleteBudgetSuccessfully() {
        when(budgetRepository.findById(1)).thenReturn(Optional.of(testBudget));
        doNothing().when(budgetRepository).delete(testBudget);

        assertDoesNotThrow(() -> budgetService.deleteById(1));

        verify(budgetRepository, times(1)).delete(testBudget);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentBudget() {
        when(budgetRepository.findById(1)).thenReturn(Optional.empty());

        BudgetNotFoundException exception = assertThrows(BudgetNotFoundException.class, () -> {
            budgetService.deleteById(1);
        });

        assertEquals("Budget with ID: 1 not found", exception.getMessage());
        verify(budgetRepository, never()).delete(any());
    }
}
