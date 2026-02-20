package lk.ijse.posbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {

    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Orders order;
    private String itemId;
    private double price;
    private int qty;
    private double total;

}
