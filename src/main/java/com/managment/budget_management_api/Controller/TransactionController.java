package com.managment.budget_management_api.Controller;

import com.managment.budget_management_api.Exceptions.TransactionNotFoundException;
import com.managment.budget_management_api.Exceptions.UserNotFoundException;
import com.managment.budget_management_api.Model.TransactionSummary;
import com.managment.budget_management_api.Service.ITransactionService;
import com.managment.budget_management_api.Service.TransactionServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.managment.budget_management_api.Model.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
@Validated
public class TransactionController {
    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private MessageSource messageSource;


    @PostMapping("/addTransaction")
    public ResponseEntity<String> addTransaction(
            @Valid @RequestBody Transaction transaction,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        try {
            transactionService.save(transaction);
            return ResponseEntity.status(HttpStatus.CREATED).body(messageSource.getMessage("response.transaction.created", null, locale));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("error.transaction.serverAdding", null, locale));
        }
    }

    @PutMapping("/update/{transactionId}")
    public ResponseEntity<String> updateTransaction(
            @Min(1) @PathVariable Integer transactionId,
            @Valid @RequestBody Transaction transaction,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        try {
            transactionService.update(transactionId, transaction);
            return ResponseEntity.ok(messageSource.getMessage("response.transaction.updated", new Object[]{transactionId}, locale));
        } catch (TransactionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("error.transaction.notfound", new Object[]{transactionId}, locale));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("error.transaction.serverUpdating", null, locale));
        }
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<?> getTransaction(
            @Min(1) @PathVariable Integer transactionId,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        try {
            return ResponseEntity.ok(transactionService.findById(transactionId)
                    .orElseThrow(() -> new TransactionNotFoundException()));
        } catch (TransactionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("error.transaction.notfound", new Object[]{transactionId}, locale));
        }
    }

    @DeleteMapping("/deleteTransaction/{transactionId}")
    public ResponseEntity<String> deleteTransaction(
            @Min(1) @PathVariable Integer transactionId,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        try {
            transactionService.deleteById(transactionId);
            return ResponseEntity.ok(messageSource.getMessage("response.transaction.deleted", new Object[]{transactionId}, locale));
        } catch (TransactionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("error.transaction.serverDeleting", null, locale));
        }
    }

    @GetMapping("/summary/{userId}")
    public ResponseEntity<?> getTransactionSummary(
            @Min(1) @PathVariable Integer userId,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        try {
            Optional<TransactionSummary> summary = transactionService.getSummary(userId);
            if (summary.isPresent()) {
                return ResponseEntity.ok(summary.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(messageSource.getMessage("error.transaction.notfound", new Object[]{userId}, locale));
            }
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageSource.getMessage("error.user.notfound", new Object[]{userId}, locale));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("error.transaction.serverRetrieving", null, locale));
        }
    }
}
