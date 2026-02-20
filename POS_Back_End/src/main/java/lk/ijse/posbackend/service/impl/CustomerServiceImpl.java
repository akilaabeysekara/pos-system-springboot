package lk.ijse.posbackend.service.impl;

import lk.ijse.posbackend.dto.CustomerDTO;
import lk.ijse.posbackend.entity.Customer;
import lk.ijse.posbackend.exception.CustomException;
import lk.ijse.posbackend.repository.CustomerRepository;
import lk.ijse.posbackend.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void saveCustomer(CustomerDTO dto) {

        if (customerRepository.existsById(dto.getId())) {
            throw new CustomException("Customer ID already exists");
        }

        customerRepository.save(modelMapper.map(dto, Customer.class));
    }

    @Override
    public void updateCustomer(CustomerDTO dto) {

        if (!customerRepository.existsById(dto.getId())) {
            throw new CustomException("Customer not found");
        }

        customerRepository.save(modelMapper.map(dto, Customer.class));
    }

    @Override
    public void deleteCustomer(String id) {

        if (!customerRepository.existsById(id)) {
            throw new CustomException("Customer not found");
        }

        customerRepository.deleteById(id);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {

        return customerRepository.findAll()
                .stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .collect(Collectors.toList());
    }
}