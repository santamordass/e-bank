package com.example.ebankbackend.enums;

public class BalanceNotSufficientException extends Throwable {
    public BalanceNotSufficientException(String balanceNotSufficient) {
        super(balanceNotSufficient);
    }
}
