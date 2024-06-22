package com.example.ebankbackend.exception;

public class CutomerNotFoundException extends Exception {
    public CutomerNotFoundException(String customerNotFound) {
        super(customerNotFound);
        }
}
