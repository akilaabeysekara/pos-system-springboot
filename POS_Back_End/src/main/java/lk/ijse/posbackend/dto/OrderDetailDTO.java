package lk.ijse.posbackend.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {

    @NotBlank(message = "Order Detail ID is required")
    @Pattern(
            regexp = "^O\\d{3}-\\d+$",
            message = "Order Detail ID must be in format O001-1"
    )
    private String id;

    @NotBlank(message = "Item ID is required")
    @Pattern(
            regexp = "^I\\d{3}$",
            message = "Item ID must be in format I001"
    )
    private String itemId;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    private Integer qty;

    @NotNull(message = "Total is required")
    @Positive(message = "Total must be greater than 0")
    private Double total;

    private LocalDateTime dateTime;
}