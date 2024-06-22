package com.example.ebankbackend.dtos;

import com.example.ebankbackend.entities.AccountOperation;
import com.example.ebankbackend.entities.Customer;
import com.example.ebankbackend.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
public class SavingBankAccountDTO extends BankAccountDTO{

    private Long id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private Double interstRate;
}
