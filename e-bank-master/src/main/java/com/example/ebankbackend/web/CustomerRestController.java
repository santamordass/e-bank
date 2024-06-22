package com.example.ebankbackend.web;

import com.example.ebankbackend.dtos.CustomerDTO;
import com.example.ebankbackend.entities.Customer;
import com.example.ebankbackend.services.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class CustomerRestController {
     private BankAccountService bankAccountService;
     @GetMapping("/customers")
     public List<CustomerDTO> customers(){
         return bankAccountService.listCustomer();
     }

     @GetMapping("/customer/{id}")
     public CustomerDTO customer(@PathVariable Long id){
         return bankAccountService.getCustomer(id);

     }
     @PostMapping("/customers")
    public Customer saveCustomer(@RequestBody CustomerDTO customerDTO){
        return bankAccountService.saveCustomer(customerDTO);
     }
    @PutMapping("customers/{id}")
    public Customer updateCustomer(@PathVariable Long id,@RequestBody CustomerDTO customerDTO){
         customerDTO.setId(id);
         return bankAccountService.updateCustomer(customerDTO);
    }
    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Long id){
         bankAccountService.deleteCustomer(id);
    }
}
