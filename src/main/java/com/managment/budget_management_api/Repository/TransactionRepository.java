package com.managment.budget_management_api.Repository;

import com.managment.budget_management_api.Model.Transaction;
import com.managment.budget_management_api.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findByUser(User user);
}
