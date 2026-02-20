package lk.ijse.posbackend.service.impl;

import lk.ijse.posbackend.dto.CustomerDTO;
import lk.ijse.posbackend.entity.Customer;
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
    public boolean saveCustomer(CustomerDTO dto) {

        if (customerRepository.existsById(dto.getId())) {
            return false;
        }

        customerRepository.save(modelMapper.map(dto, Customer.class));
        return true;
    }

    @Override
    public boolean updateCustomer(CustomerDTO dto) {

        if (!customerRepository.existsById(dto.getId())) {
            return false;
        }

        customerRepository.save(modelMapper.map(dto, Customer.class));
        return true;
    }

    @Override
    public boolean deleteCustomer(String id) {

        if (!customerRepository.existsById(id)) {
            return false;
        }

        customerRepository.deleteById(id);
        return true;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {

        return customerRepository.findAll()
                .stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .collect(Collectors.toList());
    }
}