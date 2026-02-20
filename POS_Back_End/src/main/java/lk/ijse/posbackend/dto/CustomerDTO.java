package lk.ijse.posbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerDTO {

    @NotBlank(message = "Customer ID is required")
    @Pattern(regexp = "^C\\d{3}$",
            message = "Customer ID must be in format C001")
    private String id;

    @NotBlank(message = "Customer name is required")
    @Pattern(
            regexp = "^[A-Za-z ]{3,50}$",
            message = "Name must contain only letters and spaces (3-50 characters)"
    )
    private String name;

    @NotBlank(message = "Address is required")
    @Size(min = 10, max = 100,
            message = "Address must be between 10 and 100 characters")
    private String address;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^(07\\d{8})$",
            message = "Phone number must be a valid Sri Lankan mobile number (07XXXXXXXX)"
    )
    private String phone;
}