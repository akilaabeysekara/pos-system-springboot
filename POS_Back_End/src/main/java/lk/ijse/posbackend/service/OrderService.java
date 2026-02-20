package lk.ijse.posbackend.service;

import lk.ijse.posbackend.dto.OrderDTO;

public interface OrderService {
    void PlaceOrder(OrderDTO orderDTO);

}