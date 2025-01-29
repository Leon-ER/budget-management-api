package com.managment.budget_management_api.Service;

import com.managment.budget_management_api.Exceptions.BudgetNotFoundException;
import com.managment.budget_management_api.Model.Budget;
import com.managment.budget_management_api.Repository.BudgetRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BudgetServiceImpl implements IBudgetService {
    private static final Logger logger  = LoggerFactory.getLogger(BudgetServiceImpl.class);

    private final BudgetRepository budgetRepository;

    public BudgetServiceImpl(BudgetRepository budgetRepository){
        this.budgetRepository = budgetRepository;
    }
    @Override
    public Budget save(Budget budget) {
        logger.info("Adding budget " + budget.getBudgetId());
        if(budget == null){
            throw new IllegalArgumentException("Budget cannot be null");
        }
        logger.info("Adding budget with ID: {}", budget.getBudgetId());
        try{
            return budgetRepository.save(budget);
        }catch(Exception e){
            logger.error("Error while saving user", e);
            throw new RuntimeException("Unable to save user", e);
        }

    }

    @Override
    public Budget update(Integer budgetId, Budget budgetDetails) {
        logger.info("Attempting to update budget with ID: {}", budgetId);
        if(budgetDetails == null){
            throw new IllegalArgumentException("Budget cannot be null");
        }
        Budget existingBudget = budgetRepository.findById(budgetId)
                .orElseThrow(()-> new BudgetNotFoundException(String.format("Budget with ID: %s not found", budgetId)));
        if(budgetDetails.getCategoryName() != null){
            existingBudget.setCategoryName(budgetDetails.getCategoryName());
        }
        if(budgetDetails.getTotalBudget() != null){
            existingBudget.setTotalBudget(budgetDetails.getTotalBudget());
        }
        logger.info("Budget with ID: {} updated successfully", budgetId);
        return budgetRepository.save(existingBudget);
    }

    @Override
    public Optional<Budget> findById(Integer budgetId) {
        return budgetRepository.findById(budgetId);
    }

    @Transactional
    @Override
    public void deleteById(Integer budgetId) {
        logger.info("Attempting to delete budget with ID: {}",budgetId);
        Budget existingBudget = budgetRepository.findById(budgetId)
                .orElseThrow(()-> new BudgetNotFoundException(String.format("Budget with ID: %s not found", budgetId)));
        budgetRepository.delete(existingBudget);
        logger.info("Budget with ID: {} deleted successfully", budgetId);
    }
}
