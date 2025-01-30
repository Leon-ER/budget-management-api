package com.managment.budget_management_api.Repository;

import com.managment.budget_management_api.Model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    ArrayList<Transaction> findByUser_UserId(Integer userId);

    List<Transaction> findByUser_UserIdAndTransactionDateBetween(Integer userId, LocalDateTime startDate, LocalDateTime endDate);

    List<Transaction> findByUser_UserIdAndBudget_CategoryNameAndTransactionDateBetween(
            Integer userId,
            String category,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}
