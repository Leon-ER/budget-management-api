package com.managment.budget_management_api.Service;

import com.managment.budget_management_api.Model.Budget;

import java.util.Optional;

/**
 * Interface for Budget service implementation
 */
public interface IBudgetService {

    Budget save(Budget budget);

    Budget update(Integer budgetId, Budget budgetDescription);

    Optional<Budget> findById(Integer budgetId);

    void deleteById(Integer budgetId);
}
