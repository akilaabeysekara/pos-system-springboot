package lk.ijse.posbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import lk.ijse.posbackend.service.PaymentService;
import lk.ijse.posbackend.util.APIResponse;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<APIResponse<?>> getAllPayments() {

        return ResponseEntity.ok(
                new APIResponse<>(
                        200,
                        "Payments retrieved successfully",
                        paymentService.getAllPayments()
                )
        );
    }
}