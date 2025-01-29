package com.managment.budget_management_api.Controller;

import com.managment.budget_management_api.Exceptions.BudgetNotFoundException;
import com.managment.budget_management_api.Model.Budget;
import com.managment.budget_management_api.Service.IBudgetService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budget")
@Validated
public class BudgetController {
    @Autowired
    private IBudgetService budgetService;

    @PostMapping("/addBudget")
    public ResponseEntity<String> addBudget(@Valid @RequestBody Budget budget) {
        try {
            budgetService.save(budget);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Budget created successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occured while adding a budget");
        }
    }

    @PutMapping("/update/{budgetId}")
    public ResponseEntity<?> updateBudget(@Min(1) @PathVariable Integer budgetId, @Valid @RequestBody Budget budget) {
        try {
            Budget updatedBudget = budgetService.update(budgetId, budget);
            return ResponseEntity.ok(updatedBudget);
        } catch (BudgetNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating a budget");
        }
    }

    @GetMapping("/{budgetId}")
    public ResponseEntity<Budget> getBudget(@Min(1) @PathVariable Integer budgetId) {
        return ResponseEntity.ok(budgetService.findById(budgetId)
                .orElseThrow(() -> new BudgetNotFoundException(String.format("Budget with ID: %s not found", budgetId))));
    }

    @DeleteMapping("/delete/{budgetId}")
    public ResponseEntity<String> deleteBudget(@Min(1) @PathVariable Integer budgetId) {
        try {
            budgetService.deleteById(budgetId);
            return ResponseEntity.ok(String.format("Budget with ID: %s deleted successfully", budgetId));
        } catch (BudgetNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating a budget");
        }
    }
}
