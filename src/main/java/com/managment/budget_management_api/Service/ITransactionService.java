package com.managment.budget_management_api.Service;

import com.managment.budget_management_api.Model.Transaction;
import com.managment.budget_management_api.Model.TransactionSummary;

import java.util.Optional;

public interface ITransactionService {

    Transaction save(Transaction transaction);

    Transaction update(Integer transactionID, Transaction transaction);

    Optional<Transaction> findById(Integer transactionID);

    Optional<TransactionSummary> getSummary(Integer userId);
    void deleteById(Integer transactionID);
}
