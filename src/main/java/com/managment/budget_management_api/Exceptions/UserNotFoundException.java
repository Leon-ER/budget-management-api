package com.managment.budget_management_api.Exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message){
        super(message);
    }

    public UserNotFoundException() {

    }
}
