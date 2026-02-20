package lk.ijse.posbackend.controller;

import lk.ijse.posbackend.dto.CustomerDTO;
import lk.ijse.posbackend.service.impl.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1/customer")
public class CustomerController {
    @Autowired
    private CustomerServiceImpl customerService;
    @PostMapping
    public void saveCustomer(@RequestBody CustomerDTO customerDTO){
    customerService.saveCustomer(customerDTO);
    }
    @PutMapping
    public void updateCustomer(@RequestBody CustomerDTO customerDTO){
        customerService.updateCustomer(customerDTO);
    }
    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable String id){
        customerService.deleteCustomer(id);
    }

    @GetMapping
    public Iterable<CustomerDTO> getAllCustomers(){
        return customerService.getAllCustomers();}
}
