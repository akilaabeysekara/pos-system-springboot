package lk.ijse.posbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    private String paymentId;

    private String orderId;

    private String customerName;

    @Column(length = 500)
    private String itemNames;

    private double amount;

    private String paymentMethod;

    private LocalDateTime dateTime;
}