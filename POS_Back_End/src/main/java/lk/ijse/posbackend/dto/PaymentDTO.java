package lk.ijse.posbackend.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

    @NotBlank(message = "Payment ID is required")
    private String paymentId;

    @NotBlank(message = "Order ID is required")
    private String orderId;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Item names are required")
    private String itemNames;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private Double amount;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    @NotNull(message = "Date and time is required")
    private LocalDateTime dateTime;
}