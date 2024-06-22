package com.example.ebankbackend.dtos;

import com.example.ebankbackend.entities.AccountOperation;
import lombok.Data;

import java.util.List;

@Data
public class AccountHistoryDTO {
   //d
   private Long accountId;
   private double balance;
   private int currentPage;
   private int totalPages;
   private int pageSize;
   private List<AccountOperationDTO> operationDTOS;
}
