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
    public boolean placeOrder(OrderDTO dto) {

        if (orderRepository.existsById(dto.getOrderId())) {
            return false;
        }

        Customer customer = customerRepository
                .findById(dto.getCustomerId())
                .orElse(null);

        if (customer == null) {
            return false;
        }

        // Validate all items first before saving anything
        for (OrderDetailDTO detailDTO : dto.getOrderDetails()) {

            Item item = itemRepository
                    .findById(detailDTO.getItemId())
                    .orElse(null);

            if (item == null) {
                return false;
            }

            if (item.getQty() < detailDTO.getQty()) {
                return false;
            }
        }

        Orders order = new Orders();
        order.setOrderId(dto.getOrderId());
        order.setCustomerId(dto.getCustomerId());

        orderRepository.save(order);

        for (OrderDetailDTO detailDTO : dto.getOrderDetails()) {

            Item item = itemRepository
                    .findById(detailDTO.getItemId())
                    .orElse(null);

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

        //call payment service after every thing is done
        paymentService.createPayment(dto.getOrderId());
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