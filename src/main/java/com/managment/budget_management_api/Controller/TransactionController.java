package com.managment.budget_management_api.Controller;

import com.managment.budget_management_api.Exceptions.TransactionNotFoundException;
import com.managment.budget_management_api.Service.ITransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import com.managment.budget_management_api.Model.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@Validated
public class TransactionController {
    @Autowired
    private ITransactionService transactionService;

    @PostMapping("/addTransaction")
    public ResponseEntity<String> addTransaction(@Valid @RequestBody Transaction transaction) {
        try {
            transactionService.save(transaction);
            return ResponseEntity.status(HttpStatus.CREATED).body("Transaction created successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while adding a transaction");
        }
    }

    @PutMapping("/update/{transactionId}")
    public ResponseEntity<?> updateTransaction(@Min(1) @PathVariable Integer transactionId, @Valid @RequestBody Transaction transaction) {
        try {
            Transaction updatedTransaction = transactionService.update(transactionId, transaction);
            return ResponseEntity.ok(updatedTransaction);
        } catch (TransactionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating a transaction");
        }
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransaction(@Min(1) @PathVariable Integer transactionId) {
        return ResponseEntity.ok(transactionService.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(String.format("Transaction with ID: %s not found", transactionId))));
    }

    @DeleteMapping("/deleteTransaction/{transactionId}")
    public ResponseEntity<String> deleteTransaction(@Min(1) @PathVariable Integer transactionId) {
        try {
            transactionService.deleteById(transactionId);
            return ResponseEntity.ok(String.format("Transaction with ID: %s deleted successfully", transactionId));
        } catch (TransactionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting a transaction");
        }
    }
}
