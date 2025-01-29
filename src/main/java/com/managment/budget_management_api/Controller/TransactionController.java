package com.managment.budget_management_api.Controller;

import com.managment.budget_management_api.Exceptions.TransactionNotFoundException;
import com.managment.budget_management_api.Service.ITransactionService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import com.managment.budget_management_api.Model.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private ITransactionService transactionService;

    @PostMapping("/addTransaction")
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction){
        return ResponseEntity.ok(transactionService.save(transaction));
    }
    @PutMapping("/update/{transactionId}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Integer transactionId, @RequestBody Transaction transaction){
        return ResponseEntity.ok(transactionService.update(transactionId,transaction));
    }
    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Integer transactionId){
        return ResponseEntity.ok(transactionService.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(String.format("Transaction with ID: %s not found",transactionId))));
    }
    @DeleteMapping("/deleteTransaction/{transactionId}")
    public ResponseEntity<String> deleteTransaction (@PathVariable Integer transactionId){
        transactionService.deleteById(transactionId);
        return ResponseEntity.ok(String.format("Transaction with ID: %s deleted successfully", transactionId));
    }
}
