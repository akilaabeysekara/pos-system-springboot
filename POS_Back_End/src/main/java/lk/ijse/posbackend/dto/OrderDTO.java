package lk.ijse.posbackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    @NotBlank(message = "Order ID is required")
    @Pattern(
            regexp = "^O\\d{3}$",
            message = "Order ID must be in format O001"
    )
    private String orderId;

    @NotBlank(message = "Customer ID is required")
    @Pattern(
            regexp = "^C\\d{3}$",
            message = "Customer ID must be in format C001"
    )
    private String customerId;

    @NotEmpty(message = "Order must contain at least one item")
    private List<@Valid OrderDetailDTO> orderDetails;
}