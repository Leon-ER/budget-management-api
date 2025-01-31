package com.managment.budget_management_api.Service;

import com.managment.budget_management_api.Exceptions.UserNotFoundException;
import com.managment.budget_management_api.Model.Transaction;
import com.managment.budget_management_api.Model.TransactionSummary;
import com.managment.budget_management_api.Repository.TransactionRepository;
import com.managment.budget_management_api.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;



import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class ReportGenerationService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Calculates the Income vs Expenses from the database with the given parameters and returns a
     * Transaction summary object which contains total income , total expenses and total balance
     * @param userId
     * @param startDate
     * @param endDate
     * @param category
     * @return
     */
    public TransactionSummary calculateIncomeVsExpenses(Integer userId, LocalDate startDate, LocalDate endDate, String category) {

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format("User with Id: %s not found" , userId));
        }

        // Convert LocalDate to LocalDateTime
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        // Get transactions based on user input by category or not
        List<Transaction> transactions;
        if (category != null && !category.isEmpty()) {
            transactions = transactionRepository.findByUser_UserIdAndBudget_CategoryNameAndTransactionDateBetween(userId, category, startDateTime, endDateTime);
        } else {
            transactions = transactionRepository.findByUser_UserIdAndTransactionDateBetween(userId, startDateTime, endDateTime);
        }

        if (transactions.isEmpty()) {
            throw new IllegalArgumentException("No transactions found for the given filters.");
        }
        // Iterate over the transactions list, filter by "INCOME" type, extract the amount, and calculate the total income.
        BigDecimal totalIncome = transactions.stream()
                .filter(t -> "INCOME".equalsIgnoreCase(t.getTransactionType()))
                .map(t -> t.getAmount())
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

        // Iterate over the transactions list, filter by "EXPENSE" type, extract the amount, and calculate the total income.
        BigDecimal totalExpenses = transactions.stream()
                .filter(t -> "EXPENSE".equalsIgnoreCase(t.getTransactionType()))
                .map(t -> t.getAmount())
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

        BigDecimal totalBalance = totalIncome.subtract(totalExpenses);

        return new TransactionSummary(totalIncome, totalExpenses, totalBalance);
    }
}




