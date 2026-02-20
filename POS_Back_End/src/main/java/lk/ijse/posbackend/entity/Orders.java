package lk.ijse.posbackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orders {

    @Id
    @Column(nullable = false, unique = true)
    private String orderId;
    private String customerId;
}