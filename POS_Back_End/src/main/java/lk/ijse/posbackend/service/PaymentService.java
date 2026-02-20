package lk.ijse.posbackend.service;

import lk.ijse.posbackend.dto.PaymentDTO;
import java.util.List;

public interface PaymentService {

    void createPayment(String orderId);

    List<PaymentDTO> getAllPayments();
}