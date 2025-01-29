package com.managment.budget_management_api.Controller;

import com.managment.budget_management_api.Exceptions.BudgetNotFoundException;
import com.managment.budget_management_api.Model.Budget;
import com.managment.budget_management_api.Service.BudgetServiceImpl;
import com.managment.budget_management_api.Service.IBudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budget")
public class BudgetController {
    @Autowired
    private IBudgetService budgetService;

    @PostMapping("/addBudget")
    public ResponseEntity<Budget> addBudget(@RequestBody Budget budget){
        return ResponseEntity.ok(budgetService.save(budget));
    }
    @PutMapping("/update/{budgetId}")
    public ResponseEntity<Budget> updateBudget(@PathVariable Integer budgetId, @RequestBody Budget budget){
        return ResponseEntity.ok(budgetService.update(budgetId, budget));
    }
    @GetMapping("/{budgetId}")
    public ResponseEntity<Budget> getBudget(@PathVariable Integer budgetId){
        return ResponseEntity.ok(budgetService.findById(budgetId)
                .orElseThrow(()-> new BudgetNotFoundException(String.format("Budget with ID: %s not found", budgetId))));
    }
    @DeleteMapping("/delete/{budgetId}")
    public ResponseEntity<String> deleteBudget(@PathVariable Integer budgetId){
        budgetService.deleteById(budgetId);
        return ResponseEntity.ok(String.format("Budget with ID: %s deleted successfully", budgetId));
    }
}
