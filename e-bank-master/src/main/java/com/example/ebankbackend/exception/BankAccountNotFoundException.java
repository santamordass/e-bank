package com.example.ebankbackend.exception;


public class BankAccountNotFoundException extends Throwable {
    public BankAccountNotFoundException(String message) {
        super(message);
    }
}
