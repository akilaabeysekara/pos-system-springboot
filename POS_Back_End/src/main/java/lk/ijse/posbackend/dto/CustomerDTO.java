package lk.ijse.posbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerDTO {
    @NotNull(message = "Customer Id is mandatory")
    private String id;
    @NotBlank
    @Pattern(regexp = "^\\p{L}+([ '\\-]\\p{L}+)*$", message = "Customer Name Is Invalid")
    private String name;
    @Size(min = 10, max = 100,message = "Min 10 & Max 100")
    private String address;
    @Pattern(regexp = "'+'1234567890", message = "Phone Number Is Invalid")
    @Size(min = 10, max = 10,message = "Min 10 & Max 10")
    private String phone;
}