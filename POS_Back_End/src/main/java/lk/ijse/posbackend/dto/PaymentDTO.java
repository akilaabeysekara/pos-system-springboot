package lk.ijse.posbackend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

    private String paymentId;
    private String orderId;
    private String customerName;
    private String itemNames;
    private double amount;
    private String paymentMethod;
    private LocalDateTime dateTime;
}