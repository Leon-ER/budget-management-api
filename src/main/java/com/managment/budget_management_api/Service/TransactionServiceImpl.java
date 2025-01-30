package com.managment.budget_management_api.Service;

import com.managment.budget_management_api.Exceptions.TransactionNotFoundException;
import com.managment.budget_management_api.Exceptions.UserNotFoundException;
import com.managment.budget_management_api.Model.Transaction;
import com.managment.budget_management_api.Model.TransactionSummary;
import com.managment.budget_management_api.Repository.TransactionRepository;
import com.managment.budget_management_api.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements ITransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }
        logger.info("Adding transaction with ID: {}", transaction.getTransactionId());
        try {
            return transactionRepository.save(transaction);
        } catch (Exception e) {
            logger.error("Error while saving user", e);
            throw new RuntimeException("Unable to save user", e);
        }
    }

    @Override
    public Transaction update(Integer transactionID, Transaction transactionDetails) {
        logger.info("Attempting to update transaction with ID: {}", transactionID);
        if (transactionDetails == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }
        Transaction existingTransaction = transactionRepository.findById(transactionID)
                .orElseThrow(() -> new TransactionNotFoundException(String.format("Transaction with ID: %s not found", transactionID)));
        if (transactionDetails.getAmount() != null) {
            existingTransaction.setAmount(transactionDetails.getAmount());
        }
        if (transactionDetails.getTransactionType() != null) {
            existingTransaction.setTransactionType(transactionDetails.getTransactionType());
        }
        if (transactionDetails.getDescription() != null) {
            existingTransaction.setDescription(transactionDetails.getDescription());
        }
        logger.info("Transaction with ID: {} saves successfully", transactionID);
        return transactionRepository.save(existingTransaction);
    }

    @Override
    public Optional<Transaction> findById(Integer transactionID) {
        return transactionRepository.findById(transactionID);
    }

    @Override
    public Optional<TransactionSummary> getSummary(Integer userID) {
        logger.info("Attempting to retrieve transaction summaries for user ID: {}", userID);

        if (!userRepository.existsById(userID)) {
            throw new UserNotFoundException("User with ID: " + userID + " not found");
        }
        List<Transaction> transactions = transactionRepository.findByUser_UserId(userID);
        if (transactions.isEmpty()) {
            return Optional.empty();
        }
        BigDecimal totalIncome = transactions.stream()
                .filter(t -> "INCOME".equalsIgnoreCase(t.getTransactionType()))
                .map(t -> t.getAmount())
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

        BigDecimal totalExpenses = transactions.stream()
                .filter(t -> "EXPENSE".equalsIgnoreCase(t.getTransactionType()))
                .map(t -> t.getAmount())
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

        BigDecimal totalBalance = totalIncome.subtract(totalExpenses);

        logger.info("Transaction summary calculated successfully for user ID: {}", userID);
        return Optional.of(new TransactionSummary(totalIncome, totalExpenses, totalBalance));
    }

    @Transactional
    @Override
    public void deleteById(Integer transactionID) {
        logger.info("Attempting to delete transaction with ID: {}", transactionID);
        Transaction existingTransaction = transactionRepository.findById(transactionID)
                .orElseThrow(() -> new TransactionNotFoundException(String.format("Transaction with ID: %s not found", transactionID)));
        transactionRepository.delete(existingTransaction);
        logger.info("Transaction with ID: {} deleted successfully", transactionID);
    }

}
