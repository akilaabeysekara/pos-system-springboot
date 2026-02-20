package lk.ijse.posbackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @Column(nullable = false, unique = true)
    private String id;
    private String name;
    private String address;
    private String phone;
}
