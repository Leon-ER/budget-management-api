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

    /**
     * Adds a new transaction.
     *
     * @param transaction The transaction details to be saved.
     * @param locale The locale for internationalized messages.
     * @return A response indicating success or failure of the creation operation.
     */
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

    /**
     * Updates an existing transaction based on the provided transaction ID.
     *
     * @param transactionId The ID of the transaction to be updated (must be greater than 0).
     * @param transaction The updated transaction details.
     * @param locale The locale for internationalized messages.
     * @return A response indicating success or failure of the update operation.
     */
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

    /**
     * Retrieves a transaction by its ID.
     *
     * @param transactionId The ID of the transaction to retrieve (must be greater than 0).
     * @param locale The locale for internationalized messages.
     * @return The transaction details if found, or an error message if not.
     */
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

    /**
     * Deletes a transaction by its ID.
     *
     * @param transactionId The ID of the transaction to delete (must be greater than 0).
     * @param locale The locale for internationalized messages.
     * @return A response indicating success or failure of the delete operation.
     */
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

    /**
     * Retrieves a summary of transactions for a specific user.
     *
     * @param userId The ID of the user whose transaction summary is requested (must be greater than 0).
     * @param locale The locale for internationalized messages.
     * @return A summary of transactions if found, or an error message if not.
     */
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
