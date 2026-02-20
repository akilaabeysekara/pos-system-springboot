package lk.ijse.posbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @Column(nullable = false, unique = true)
    private String id;
    private String name;
    private double price;
    private int qty;
}