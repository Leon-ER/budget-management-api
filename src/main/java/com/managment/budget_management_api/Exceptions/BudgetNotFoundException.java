package com.managment.budget_management_api.Exceptions;

/**
 * Custom exception for when Budget not found
 */
public class BudgetNotFoundException extends RuntimeException {
    public BudgetNotFoundException (String message){
        super(message);
    }

    public BudgetNotFoundException() {

    }
}
