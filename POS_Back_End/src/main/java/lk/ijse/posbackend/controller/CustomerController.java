package lk.ijse.posbackend.controller;

import lk.ijse.posbackend.dto.CustomerDTO;
import lk.ijse.posbackend.service.CustomerService;
import lk.ijse.posbackend.util.APIResponse;
import lombok.RequiredArgsConstructor;
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
            @RequestBody CustomerDTO dto) {

        if (!customerService.saveCustomer(dto)) {
            return ResponseEntity
                    .status(400)
                    .body(new APIResponse<>(400,
                            "Customer ID already exists",
                            null));
        }

        return ResponseEntity
                .status(201)
                .body(new APIResponse<>(201,
                        "Customer saved successfully",
                        null));
    }

    @PutMapping
    public ResponseEntity<APIResponse<String>> updateCustomer(
            @RequestBody CustomerDTO dto) {

        if (!customerService.updateCustomer(dto)) {
            return ResponseEntity
                    .status(404)
                    .body(new APIResponse<>(404,
                            "Customer not found",
                            null));
        }

        return ResponseEntity.ok(
                new APIResponse<>(200,
                        "Customer updated successfully",
                        null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<String>> deleteCustomer(
            @PathVariable String id) {

        if (!customerService.deleteCustomer(id)) {
            return ResponseEntity
                    .status(404)
                    .body(new APIResponse<>(404,
                            "Customer not found",
                            null));
        }

        return ResponseEntity.ok(
                new APIResponse<>(200,
                        "Customer deleted successfully",
                        null));
    }

    @GetMapping
    public ResponseEntity<APIResponse<?>> getAllCustomers() {

        return ResponseEntity.ok(
                new APIResponse<>(200,
                        "Success",
                        customerService.getAllCustomers()));
    }
}