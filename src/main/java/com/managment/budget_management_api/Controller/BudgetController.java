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
import org.springframework.context.MessageSource;


import java.util.Locale;

@RestController
@RequestMapping("/api/budget")
@Validated
public class BudgetController {
    @Autowired
    private IBudgetService budgetService;

    @Autowired
    private MessageSource messageSource;

    /**
     * Adds a new budget entry.
     *
     * @param budget The budget details to be saved.
     * @param locale The locale for internationalized messages.
     * @return A response indicating success or failure of the creation operation.
     */
    @PostMapping("/addBudget")
    public ResponseEntity<String> addBudget(
            @Valid @RequestBody Budget budget,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        try {
            budgetService.save(budget);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(messageSource.getMessage("response.budget.created", null, locale));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("error.budget.serverAdding", null, locale));
        }
    }

    /**
     * Updates an existing budget entry based on the provided budget ID.
     *
     * @param budgetId The ID of the budget to be updated (must be greater than 0).
     * @param budget The updated budget details.
     * @param locale The locale for internationalized messages.
     * @return A response indicating success or failure of the update operation.
     */
    @PutMapping("/update/{budgetId}")
    public ResponseEntity<String> updateBudget(
            @Min(1) @PathVariable Integer budgetId,
            @Valid @RequestBody Budget budget,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        try {
            budgetService.update(budgetId, budget);
            return ResponseEntity.ok(messageSource.getMessage("response.budget.updated", new Object[]{budgetId}, locale));
        } catch (BudgetNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("error.budget.notfound", new Object[]{budgetId}, locale));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("error.budget.serverUpdating", null, locale));
        }
    }

    /**
     * Retrieves a budget by its ID.
     *
     * @param budgetId The ID of the budget to retrieve (must be greater than 0).
     * @param locale The locale for internationalized messages.
     * @return The budget details if found, or an error message if not.
     */
    @GetMapping("/{budgetId}")
    public ResponseEntity<?> getBudget(
            @Min(1) @PathVariable Integer budgetId,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        try {
            return ResponseEntity.ok(budgetService.findById(budgetId)
                    .orElseThrow(() -> new BudgetNotFoundException()));
        } catch (BudgetNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("error.budget.notfound", new Object[]{budgetId}, locale));
        }

    }

    /**
     * Deletes a budget by its ID.
     *
     * @param budgetId The ID of the budget to delete (must be greater than 0).
     * @param locale The locale for internationalized messages.
     * @return A response indicating success or failure of the delete operation.
     */
    @DeleteMapping("/delete/{budgetId}")
    public ResponseEntity<String> deleteBudget(
            @Min(1) @PathVariable Integer budgetId,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        try {
            budgetService.deleteById(budgetId);
            return ResponseEntity.ok(messageSource.getMessage("response.budget.deleted", new Object[]{budgetId}, locale));
        } catch (BudgetNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("error.budget.notfound", new Object[]{budgetId}, locale));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("error.budget.serverDeleting", null, locale));
        }
    }
}
