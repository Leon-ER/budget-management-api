package com.managment.budget_management_api.Exceptions;

public class BudgetNotFoundException extends RuntimeException {
    public BudgetNotFoundException (String message){
        super(message);
    }

    public BudgetNotFoundException() {

    }
}
