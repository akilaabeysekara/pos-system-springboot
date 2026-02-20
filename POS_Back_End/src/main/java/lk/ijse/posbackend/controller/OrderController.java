package lk.ijse.posbackend.controller;

import lk.ijse.posbackend.dto.OrderDTO;
import lk.ijse.posbackend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> saveOrder(@RequestBody OrderDTO orderDTO) {

        boolean result = orderService.placeOrder(orderDTO);

        if (!result) {
            return ResponseEntity
                    .badRequest()
                    .body("Order ID already exists");
        }

        return ResponseEntity
                .status(201)
                .body("Order placed successfully");
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }



}