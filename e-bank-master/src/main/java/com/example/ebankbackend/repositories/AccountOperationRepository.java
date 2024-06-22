package com.example.ebankbackend.repositories;

import com.example.ebankbackend.entities.AccountOperation;
import com.example.ebankbackend.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface AccountOperationRepository extends JpaRepository<AccountOperation,Long> {
    List<AccountOperation> findByBankAccount_Id(Long accountId);
     Page<AccountOperation> findByBankAccount_Id(Long accountId, Pageable pageable);
}
