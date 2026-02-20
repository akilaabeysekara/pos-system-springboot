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

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;

    @Override
    public void PlaceOrder(OrderDTO dto) {

        // CHECK ORDER ID DUPLICATE
        if (orderRepository.existsById(dto.getOrderId())) {
            throw new RuntimeException("Order ID already exists: " + dto.getOrderId());
        }

        //  VALIDATE CUSTOMER
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() ->
                        new RuntimeException("Customer not found with ID: " + dto.getCustomerId()));

        // SAVE ORDER
        Orders order = new Orders();
        order.setOrderId(dto.getOrderId());
        order.setCustomerId(dto.getCustomerId());

        orderRepository.save(order);

        // SAVE ORDER DETAILS
        for (OrderDetailDTO detailDTO : dto.getOrderDetails()) {

            //  CHECK ORDER DETAIL DUPLICATE
            if (orderDetailRepository.existsById(detailDTO.getId())) {
                throw new RuntimeException("Order Detail ID already exists: " + detailDTO.getId());
            }

            //  VALIDATE ITEM
            Item item = itemRepository.findById(detailDTO.getItemId())
                    .orElseThrow(() ->
                            new RuntimeException("Item not found with ID: " + detailDTO.getItemId()));

            //  CHECK STOCK
            if (item.getQty() < detailDTO.getQty()) {
                throw new RuntimeException(
                        "Insufficient stock for item: " + detailDTO.getItemId() +
                                ". Available: " + item.getQty() +
                                ", Required: " + detailDTO.getQty()
                );
            }

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setId(detailDTO.getId());
            orderDetail.setItemId(detailDTO.getItemId());
            orderDetail.setPrice(detailDTO.getPrice());
            orderDetail.setQty(detailDTO.getQty());
            orderDetail.setTotal(detailDTO.getTotal());

            orderDetailRepository.save(orderDetail);

            //  UPDATE STOCK
            item.setQty(item.getQty() - detailDTO.getQty());
            itemRepository.save(item);
        }
    }
}