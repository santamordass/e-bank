package com.example.ebankbackend;

import com.example.ebankbackend.dtos.CustomerDTO;
import com.example.ebankbackend.entities.*;
import com.example.ebankbackend.enums.AccountStatus;
import com.example.ebankbackend.enums.BalanceNotSufficientException;
import com.example.ebankbackend.enums.OperationType;
import com.example.ebankbackend.exception.BankAccountNotFoundException;
import com.example.ebankbackend.exception.CutomerNotFoundException;
import com.example.ebankbackend.repositories.AccountOperationRepository;
import com.example.ebankbackend.repositories.BankAccountRepository;
import com.example.ebankbackend.repositories.CustomerRepository;
import com.example.ebankbackend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankBackendApplication.class, args);
    }

   // @Bean
/*    CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
        return args -> {
            Stream.of("moad", "mouha", "modric").forEach(name -> {
                CustomerDTO customer = new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name + "mail.ma");
                bankAccountService.saveCustomer(customer);
            });

            bankAccountService.listCustomer().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random() * 900000, 900, customer.getId());
                } catch (CutomerNotFoundException e) {
                    throw new RuntimeException(e);
                }

                try {
                    bankAccountService.saveSavingBankAccount(Math.random() * 1200000, 5.5, customer.getId());
                } catch (CutomerNotFoundException e) {
                    e.printStackTrace();
                }

                bankAccountService.bankAccountList().forEach(account -> {
                    for (int i = 0; i < 10; i++) {
                        try {
                            bankAccountService.credit(account.getId(), 10000 + Math.random() * 12000, "Credit");
                            bankAccountService.debit(account.getId(), 10000 + Math.random() * 9000, "Debit");
                        } catch (BankAccountNotFoundException e) {
                            e.printStackTrace();
                        } catch (BalanceNotSufficientException e) {
                            System.err.println("Error: Balance not sufficient for account " + account.getId() + " during debit operation.");
                        }
                    }
                });
            });
        };
    }*/

    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository
    , AccountOperationRepository accountOperationRepository){
        return args ->{
            Stream.of("Hassan","jawad","Sma9lo").forEach(name->{
                Customer customer=new Customer();
                customer.setName(name);
                customer.setEmail(name+"@mail.ma");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(Customer->{
                CurrentAccount currentAccount=new CurrentAccount();
                currentAccount.setBalance(Math.round(Math.random()*9000));
                currentAccount.setCustomer(Customer);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);


                SavingAccount savingAccount=new SavingAccount();
                savingAccount.setBalance(Math.round(Math.random()*9000));
                savingAccount.setCustomer(Customer);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setInterestRate(4.5);
                bankAccountRepository.save(savingAccount);

            });
            bankAccountRepository.findAll().forEach(acc->{
                for (int i=0;i<5;i++){
                    AccountOperation accountOperation=new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.round(Math.random()*3000));
                    accountOperation.setType(Math.random()>0.5 ? OperationType.DEBIT:OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }
            });
        };
    }
}
