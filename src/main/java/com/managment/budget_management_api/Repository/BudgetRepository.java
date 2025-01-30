package com.managment.budget_management_api.Repository;

import com.managment.budget_management_api.Model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    List<Budget> findByUser_UserId(Integer userId);
}
