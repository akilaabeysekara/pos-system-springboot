package lk.ijse.posbackend.controller;

import lk.ijse.posbackend.dto.OrderDTO;
import lk.ijse.posbackend.service.OrderService;
import lk.ijse.posbackend.util.APIResponse;
import lombok.RequiredArgsConstructor;
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
            @RequestBody OrderDTO orderDTO) {

        if (!orderService.placeOrder(orderDTO)) {

            return ResponseEntity
                    .status(400)
                    .body(new APIResponse<>(
                            400,
                            "Order failed: Duplicate ID or insufficient stock",
                            null));
        }

        return ResponseEntity
                .status(201)
                .body(new APIResponse<>(
                        201,
                        "Order placed successfully",
                        null));
    }

    @GetMapping
    public ResponseEntity<APIResponse<?>> getAllOrders() {

        return ResponseEntity.ok(
                new APIResponse<>(
                        200,
                        "Success",
                        orderService.getAllOrders()));
    }

    @GetMapping("/next-id")
    public ResponseEntity<?> getNextOrderId() {

        return ResponseEntity.ok(
                new APIResponse<>(
                        200,
                        "Success",
                        orderService.generateNextOrderId()
                ));
    }
}