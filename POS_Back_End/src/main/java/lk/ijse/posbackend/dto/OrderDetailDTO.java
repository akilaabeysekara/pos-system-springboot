package lk.ijse.posbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    private String id;
    private String itemId;
    private double price;
    private int qty;
    private double total;
    private LocalDateTime dateTime;}