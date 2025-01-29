package com.managment.budget_management_api.Service;

import com.managment.budget_management_api.Exceptions.TransactionNotFoundException;
import com.managment.budget_management_api.Model.Transaction;
import com.managment.budget_management_api.Repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionServiceImpl implements ITransactionService{
    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        if(transaction == null){
            throw new IllegalArgumentException("Transaction cannot be null");
        }
        logger.info("Adding transaction with ID: {}",transaction.getTransactionId());
        try{
            return transactionRepository.save(transaction);
        }catch (Exception e){
            logger.error("Error while saving user", e);
            throw new RuntimeException("Unable to save user", e);
        }
    }

    @Override
    public Transaction update(Integer transactionID, Transaction transactionDetails) {
        logger.info("Attempting to update transaction with ID: {}", transactionID);
        if(transactionDetails == null){
            throw new IllegalArgumentException("Transaction cannot be null");
        }
        Transaction existingTransaction = transactionRepository.findById(transactionID)
                .orElseThrow(() -> new TransactionNotFoundException(String.format("Transaction with ID: %s not found",transactionID)));
        if(transactionDetails.getAmount() != null){
            existingTransaction.setAmount(transactionDetails.getAmount());
        }
        if(transactionDetails.getTransactionType() != null){
            existingTransaction.setTransactionType(transactionDetails.getTransactionType());
        }
        if(transactionDetails.getDescription() != null){
            existingTransaction.setDescription(transactionDetails.getDescription());
        }
        logger.info("Transaction with ID: {} saves successfully",transactionID);
        return transactionRepository.save(existingTransaction);
    }

    @Override
    public Optional<Transaction> findById(Integer transactionID) {
        return transactionRepository.findById(transactionID);
    }

    @Transactional
    @Override
    public void deleteById(Integer transactionID) {
        logger.info("Attempding to delete transaction with ID: {}", transactionID);
        Transaction existingTransaction = transactionRepository.findById(transactionID)
                .orElseThrow(() -> new TransactionNotFoundException(String.format("Transaction with ID: %s not found",transactionID)));
        transactionRepository.delete(existingTransaction);
        logger.info("Transaction with ID: {} deleted successfully",transactionID);
    }
}
