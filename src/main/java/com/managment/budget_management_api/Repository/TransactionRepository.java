package com.managment.budget_management_api.Repository;

import com.managment.budget_management_api.Model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

}
