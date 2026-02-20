package lk.ijse.posbackend.service;

import lk.ijse.posbackend.dto.OrderDTO;

import java.util.List;

public interface OrderService {

    String generateNextOrderId();
    boolean placeOrder(OrderDTO orderDTO);
    List<OrderDTO> getAllOrders();


}