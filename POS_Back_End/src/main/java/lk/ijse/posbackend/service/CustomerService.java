package lk.ijse.posbackend.service;

import lk.ijse.posbackend.dto.CustomerDTO;
import java.util.List;

public interface CustomerService {

    boolean saveCustomer(CustomerDTO dto);

    boolean updateCustomer(CustomerDTO dto);

    boolean deleteCustomer(String id);

    List<CustomerDTO> getAllCustomers();
}