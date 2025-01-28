package com.managment.budget_management_api;

import com.managment.budget_management_api.Model.Budget;
import com.managment.budget_management_api.Model.Transaction;
import com.managment.budget_management_api.Model.User;
import com.managment.budget_management_api.Repository.BudgetRepository;
import com.managment.budget_management_api.Repository.TransactionRepository;
import com.managment.budget_management_api.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class TestDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;

    public TestDataLoader(UserRepository userRepository,
                          BudgetRepository budgetRepository,
                          TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.budgetRepository = budgetRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(String... args) {
        User user = new User();
        user.setUsername("john_doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("securepassword");
        user.setRole("USER");
        userRepository.save(user);

        Budget budget = new Budget();
        budget.setCategoryName("Groceries");
        budget.setTotalBudget(new BigDecimal("500.00"));
        budget.setUser(user);
        budgetRepository.save(budget);

        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setDescription("Bought groceries");
        transaction.setTransactionType("EXPENSE");
        transaction.setUser(user);
        transaction.setBudget(budget);
        transactionRepository.save(transaction);

        System.out.println("Test data inserted successfully!");
    }
}

