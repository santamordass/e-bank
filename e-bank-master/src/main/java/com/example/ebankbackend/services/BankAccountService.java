package com.example.ebankbackend.services;

import com.example.ebankbackend.dtos.*;
import com.example.ebankbackend.entities.Customer;
import com.example.ebankbackend.enums.BalanceNotSufficientException;
import com.example.ebankbackend.exception.BankAccountNotFoundException;
import com.example.ebankbackend.exception.CutomerNotFoundException;

import java.util.List;

public interface BankAccountService {
     Customer saveCustomer(CustomerDTO customer);
    Customer updateCustomer(CustomerDTO customer);
    void deleteCustomer(Long id);
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overdraft, Long customerId) throws CutomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CutomerNotFoundException;
    List<CustomerDTO> listCustomer();
     BankAccountDTO getBankAccount(Long id)throws BankAccountNotFoundException;
     void debit(Long accountId,double amount,String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(Long accountId,double amount,String description) throws BankAccountNotFoundException;
    void  transfer(Long accountIdSource,Long accountIdDestination,double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
      CustomerDTO getCustomer(Long id);
    List<BankAccountDTO> bankAccountList();
     List<AccountOperationDTO> historique(Long accID);

    AccountHistoryDTO getAccountHistory(Long id, int page, int size) throws BankAccountNotFoundException;
}
