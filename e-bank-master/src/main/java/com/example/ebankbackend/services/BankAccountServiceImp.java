package com.example.ebankbackend.services;

import com.example.ebankbackend.dtos.*;
import com.example.ebankbackend.entities.*;
import com.example.ebankbackend.enums.BalanceNotSufficientException;
import com.example.ebankbackend.enums.OperationType;
import com.example.ebankbackend.exception.BankAccountNotFoundException;
import com.example.ebankbackend.exception.CutomerNotFoundException;
import com.example.ebankbackend.mappers.BankAccountMapper;
import com.example.ebankbackend.repositories.AccountOperationRepository;
import com.example.ebankbackend.repositories.BankAccountRepository;
import com.example.ebankbackend.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImp implements  BankAccountService {
 private CustomerRepository customerRepository;
  private BankAccountRepository bankAccountRepository;
  private BankAccountMapper dtoMapper;
    private static final AtomicLong TS = new AtomicLong();
 private AccountOperationRepository accountOperationRepository;



    @Override
    public Customer saveCustomer(CustomerDTO customer) {
        log.info("new customer");
        return customerRepository.save(dtoMapper.fromCustomerDTO(customer));
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overdraft, Long customerId) throws CutomerNotFoundException {
   Customer customer=customerRepository.findById(customerId).orElse(null);
            if (customer==null){
                throw new CutomerNotFoundException("Customer not found");
            }
            CurrentAccount bankAccount= new CurrentAccount();
            bankAccount.setId(TS.incrementAndGet());
            bankAccount.setCreatedAt(new Date());
            bankAccount.setBalance(initialBalance);
            bankAccount.setOverDraft(overdraft);
            bankAccount.setCustomer(customer);

            return   dtoMapper.fromCurrentBankAccount(bankAccountRepository.save(bankAccount));
        }


    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CutomerNotFoundException {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if (customer==null){
            throw new CutomerNotFoundException("Customer not found");
        }
        SavingAccount bankAccount= new SavingAccount();
        bankAccount.setId(TS.incrementAndGet());
        bankAccount.setCreatedAt(new Date());
        bankAccount.setBalance(initialBalance);
        bankAccount.setInterestRate(interestRate);
        bankAccount.setCustomer(customer);

        return  dtoMapper.fromSavingAccount(bankAccountRepository.save(bankAccount));

    }


    @Override
    public List<CustomerDTO> listCustomer() {
        return customerRepository.findAll().stream().map(cust->dtoMapper.fromCustomer(cust)).collect(Collectors.toList());
    }

    @Override
    public BankAccountDTO getBankAccount(Long id) throws BankAccountNotFoundException {

        BankAccount bankAccount= bankAccountRepository.findById(id).orElseThrow(()->new BankAccountNotFoundException("not found"));
        if (bankAccount instanceof SavingAccount){
            SavingAccount savingAccount=(SavingAccount) bankAccount;
            return dtoMapper.fromSavingAccount(savingAccount);
        }
        else{
            CurrentAccount currentAccount=(CurrentAccount) bankAccount;
        return     dtoMapper.fromCurrentBankAccount(currentAccount);
        }

    }

    @Override
    public void debit(Long accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount= bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("not found"));
        if(bankAccount.getBalance()<amount)
            throw new BalanceNotSufficientException("Balance not sufficient");
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(Long accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount= bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("not found"));

        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(Long accountIdSource, Long accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource,amount,"transfert from"+accountIdSource);
        credit(accountIdDestination,amount,"transfert to"+accountIdDestination);
    }

    @Override
    public List<BankAccountDTO> bankAccountList(){
        return bankAccountRepository.findAll().stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount){
                return dtoMapper.fromSavingAccount((SavingAccount) bankAccount);
            }
           return dtoMapper.fromCurrentBankAccount((CurrentAccount) bankAccount);
        }).collect(Collectors.toList());
    }
    public  CustomerDTO getCustomer(Long id){
        return dtoMapper.fromCustomer(customerRepository.findById(id).orElseThrow(()->new RuntimeException("not found")));
    }
    @Override
    public Customer updateCustomer(CustomerDTO customer) {
        log.info("new customer");
        return customerRepository.save(dtoMapper.fromCustomerDTO(customer));
    }
    @Override
    public void deleteCustomer(Long id){
        customerRepository.deleteById(id);
    }
    @Override
    public List<AccountOperationDTO> historique(Long accID){
      return  accountOperationRepository.findByBankAccount_Id(accID).stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
    }
    @Override
    public  AccountHistoryDTO getAccountHistory(Long id, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(id).orElse(null);
        if (bankAccount==null) throw  new BankAccountNotFoundException("NO BANK ACC");
        Page<AccountOperation> accountOperations= accountOperationRepository.findByBankAccount_Id(id,PageRequest.of(page,size));
        AccountHistoryDTO accountHistoryDTO=new AccountHistoryDTO();
        accountHistoryDTO.setOperationDTOS(accountOperations.getContent().stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList()));
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setPageSize(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return  accountHistoryDTO;
    }
}
