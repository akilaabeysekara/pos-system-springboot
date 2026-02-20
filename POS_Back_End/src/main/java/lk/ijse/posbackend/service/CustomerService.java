package lk.ijse.posbackend.service;

import lk.ijse.posbackend.dto.CustomerDTO;
import java.util.List;

public interface CustomerService {

    void saveCustomer(CustomerDTO dto);
    void updateCustomer(CustomerDTO dto);
    void deleteCustomer(String id);
    List<CustomerDTO> getAllCustomers();

}