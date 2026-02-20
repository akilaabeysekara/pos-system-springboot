package lk.ijse.posbackend.service.impl;

import lk.ijse.posbackend.dto.CustomerDTO;
import lk.ijse.posbackend.entity.Customer;
import lk.ijse.posbackend.repository.CustomerRepository;
import lk.ijse.posbackend.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void saveCustomer(CustomerDTO customerDTO) {
        // Prevent duplicate customer IDs
        if (customerRepository.existsById(customerDTO.getId())) {
            throw new RuntimeException("Customer ID already exists");
        }

        Customer customer = modelMapper.map(customerDTO, Customer.class);
        customerRepository.save(customer);
    }

    @Override
    public void updateCustomer(CustomerDTO customerDTO) {
        // Ensure customer exists before updating
        if (!customerRepository.existsById(customerDTO.getId())) {
            throw new RuntimeException("Customer not found");
        }

        Customer customer = modelMapper.map(customerDTO, Customer.class);
        customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(String id) {
        // Ensure customer exists before deleting
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found");
        }

        customerRepository.deleteById(id);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        // Convert entity list to DTO list
        return customerRepository.findAll()
                .stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .collect(Collectors.toList());
    }
}