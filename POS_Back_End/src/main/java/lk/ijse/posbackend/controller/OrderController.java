package lk.ijse.posbackend.controller;

import jakarta.validation.Valid;
import lk.ijse.posbackend.dto.OrderDTO;
import lk.ijse.posbackend.service.OrderService;
import lk.ijse.posbackend.util.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<APIResponse<String>> saveOrder(
            @RequestBody @Valid OrderDTO orderDTO) {

        orderService.placeOrder(orderDTO);

        return new ResponseEntity<>(
                new APIResponse<>(
                        201,
                        "Order placed successfully",
                        null
                ),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<APIResponse<?>> getAllOrders() {

        return ResponseEntity.ok(
                new APIResponse<>(
                        200,
                        "Success",
                        orderService.getAllOrders()
                )
        );
    }

    @GetMapping("/next-id")
    public ResponseEntity<APIResponse<String>> getNextOrderId() {

        return ResponseEntity.ok(
                new APIResponse<>(
                        200,
                        "Success",
                        orderService.generateNextOrderId()
                )
        );
    }
}