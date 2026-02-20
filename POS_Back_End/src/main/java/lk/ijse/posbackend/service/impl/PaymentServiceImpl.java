package lk.ijse.posbackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lk.ijse.posbackend.entity.*;
import lk.ijse.posbackend.dto.*;
import lk.ijse.posbackend.repository.*;
import lk.ijse.posbackend.service.PaymentService;
import lk.ijse.posbackend.exception.CustomException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void createPayment(String orderId) {

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new CustomException("Order not found for payment"));

        Customer customer = customerRepository
                .findById(order.getCustomerId())
                .orElseThrow(() ->
                        new CustomException("Customer not found for payment"));

        List<OrderDetail> details =
                orderDetailRepository.findAll()
                        .stream()
                        .filter(d -> d.getOrder().getOrderId().equals(orderId))
                        .toList();

        if (details.isEmpty()) {
            throw new CustomException("Order has no items");
        }

        String itemNames = details.stream()
                .map(OrderDetail::getItemId)
                .collect(Collectors.joining(", "));

        double totalAmount = details.stream()
                .mapToDouble(OrderDetail::getTotal)
                .sum();

        String nextId = generateNextPaymentId();

        Payment payment = new Payment(
                nextId,
                orderId,
                customer.getName(),
                itemNames,
                totalAmount,
                "CASH",
                LocalDateTime.now()
        );

        paymentRepository.save(payment);
    }

    private String generateNextPaymentId() {

        Payment last = paymentRepository.findTopByOrderByPaymentIdDesc();

        if (last == null) return "P001";

        int num = Integer.parseInt(last.getPaymentId().substring(1));
        return String.format("P%03d", num + 1);
    }

    @Override
    public List<PaymentDTO> getAllPayments() {

        return paymentRepository.findAll()
                .stream()
                .map(p -> new PaymentDTO(
                        p.getPaymentId(),
                        p.getOrderId(),
                        p.getCustomerName(),
                        p.getItemNames(),
                        p.getAmount(),
                        p.getPaymentMethod(),
                        p.getDateTime()
                ))
                .toList();
    }
}