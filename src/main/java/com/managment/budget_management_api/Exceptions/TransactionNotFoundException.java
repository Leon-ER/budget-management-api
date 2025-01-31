package com.managment.budget_management_api.Exceptions;

/**
 * Custom exception for when Transaction not found
 */
public class TransactionNotFoundException extends RuntimeException{
    public TransactionNotFoundException(String message){
        super(message);
    }

    public TransactionNotFoundException() {

    }
}
