package lk.ijse.posbackend.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.posbackend.dto.OrderDTO;
import lk.ijse.posbackend.dto.OrderDetailDTO;
import lk.ijse.posbackend.entity.*;
import lk.ijse.posbackend.exception.CustomException;
import lk.ijse.posbackend.repository.*;
import lk.ijse.posbackend.service.OrderService;
import lk.ijse.posbackend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;
    private final PaymentService paymentService;

    @Override
    public String generateNextOrderId() {

        Orders lastOrder = orderRepository.findTopByOrderByOrderIdDesc();

        if (lastOrder == null) {
            return "O001";
        }

        int num = Integer.parseInt(lastOrder.getOrderId().substring(1));
        return String.format("O%03d", num + 1);
    }

    @Override
    public void placeOrder(OrderDTO dto) {

        if (orderRepository.existsById(dto.getOrderId())) {
            throw new CustomException("Order ID already exists");
        }

        Customer customer = customerRepository
                .findById(dto.getCustomerId())
                .orElseThrow(() -> new CustomException("Customer not found"));

        for (OrderDetailDTO detailDTO : dto.getOrderDetails()) {

            Item item = itemRepository
                    .findById(detailDTO.getItemId())
                    .orElseThrow(() ->
                            new CustomException("Item not found: " + detailDTO.getItemId()));

            if (item.getQty() < detailDTO.getQty()) {
                throw new CustomException(
                        "Insufficient stock for item: " + detailDTO.getItemId());
            }
        }

        Orders order = new Orders();
        order.setOrderId(dto.getOrderId());
        order.setCustomerId(dto.getCustomerId());

        orderRepository.save(order);

        for (OrderDetailDTO detailDTO : dto.getOrderDetails()) {

            Item item = itemRepository.findById(detailDTO.getItemId()).get();

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setId(detailDTO.getId());
            orderDetail.setItemId(detailDTO.getItemId());
            orderDetail.setPrice(detailDTO.getPrice());
            orderDetail.setQty(detailDTO.getQty());
            orderDetail.setTotal(detailDTO.getTotal());
            orderDetail.setDateTime(LocalDateTime.now());

            orderDetailRepository.save(orderDetail);

            item.setQty(item.getQty() - detailDTO.getQty());
            itemRepository.save(item);
        }

        paymentService.createPayment(dto.getOrderId());
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