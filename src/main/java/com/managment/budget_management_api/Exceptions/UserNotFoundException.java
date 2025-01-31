package com.managment.budget_management_api.Exceptions;

/**
 * Custom exception for when User not found
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message){
        super(message);
    }

    public UserNotFoundException() {

    }
}
