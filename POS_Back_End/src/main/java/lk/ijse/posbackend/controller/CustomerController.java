package lk.ijse.posbackend.controller;

import jakarta.validation.Valid;
import lk.ijse.posbackend.dto.CustomerDTO;
import lk.ijse.posbackend.service.CustomerService;
import lk.ijse.posbackend.util.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<APIResponse<String>> saveCustomer(
            @RequestBody @Valid CustomerDTO customerDTO) {

        customerService.saveCustomer(customerDTO);

        return new ResponseEntity<>(
                new APIResponse<>(
                        201,
                        "Customer Saved",
                        null
                ),
                HttpStatus.CREATED
        );
    }

    @PutMapping
    public ResponseEntity<APIResponse<String>> updateCustomer(
            @RequestBody @Valid CustomerDTO customerDTO) {

        customerService.updateCustomer(customerDTO);

        return ResponseEntity.ok(
                new APIResponse<>(
                        200,
                        "Customer Updated Successfully",
                        null
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<String>> deleteCustomer(
            @PathVariable String id) {

        customerService.deleteCustomer(id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        200,
                        "Customer Deleted Successfully",
                        null
                )
        );
    }

    @GetMapping
    public ResponseEntity<APIResponse<?>> getAllCustomers() {

        return ResponseEntity.ok(
                new APIResponse<>(
                        200,
                        "Success",
                        customerService.getAllCustomers()
                )
        );
    }
}