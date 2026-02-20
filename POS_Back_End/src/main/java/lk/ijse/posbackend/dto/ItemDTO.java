package lk.ijse.posbackend.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {

    @NotBlank(message = "Item ID is required")
    @Pattern(
            regexp = "^I\\d{3}$",
            message = "Item ID must be in format I001"
    )
    private String id;

    @NotBlank(message = "Item name is required")
    @Size(min = 3, max = 50,
            message = "Item name must be between 3 and 50 characters")
    private String name;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer qty;
}