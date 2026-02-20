package lk.ijse.posbackend.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.posbackend.dto.OrderDTO;
import lk.ijse.posbackend.dto.OrderDetailDTO;
import lk.ijse.posbackend.entity.Customer;
import lk.ijse.posbackend.entity.Item;
import lk.ijse.posbackend.entity.Orders;
import lk.ijse.posbackend.entity.OrderDetail;
import lk.ijse.posbackend.repository.CustomerRepository;
import lk.ijse.posbackend.repository.ItemRepository;
import lk.ijse.posbackend.repository.OrderRepository;
import lk.ijse.posbackend.repository.OrderDetailRepository;
import lk.ijse.posbackend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;

    @Override
    public boolean placeOrder(OrderDTO dto) {

        // Return false if order ID already exists
        if (orderRepository.existsById(dto.getOrderId())) {
            return false;
        }

        // Validate customer existence
        Customer customer = customerRepository
                .findById(dto.getCustomerId())
                .orElse(null);

        if (customer == null) {
            return false;
        }

        Orders order = new Orders();
        order.setOrderId(dto.getOrderId());
        order.setCustomerId(dto.getCustomerId());

        orderRepository.save(order);

        for (OrderDetailDTO detailDTO : dto.getOrderDetails()) {

            // Prevent duplicate order detail ID
            if (orderDetailRepository.existsById(detailDTO.getId())) {
                return false;
            }

            Item item = itemRepository
                    .findById(detailDTO.getItemId())
                    .orElse(null);

            if (item == null) {
                return false;
            }

            // Validate stock before deduction
            if (item.getQty() < detailDTO.getQty()) {
                return false;
            }

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setId(detailDTO.getId());
            orderDetail.setItemId(detailDTO.getItemId());
            orderDetail.setPrice(detailDTO.getPrice());
            orderDetail.setQty(detailDTO.getQty());
            orderDetail.setTotal(detailDTO.getTotal());
            orderDetail.setDate(detailDTO.getDate());

            orderDetailRepository.save(orderDetail);

            // Update item stock
            item.setQty(item.getQty() - detailDTO.getQty());
            itemRepository.save(item);
        }

        return true;
    }
    @Override
    public List<OrderDTO> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(order -> {
                    OrderDTO dto = new OrderDTO();
                    dto.setOrderId(order.getOrderId());
                    dto.setCustomerId(order.getCustomerId());
                    return dto;
                })
                .toList();
    }
}