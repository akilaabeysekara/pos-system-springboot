package lk.ijse.posbackend.controller;

import jakarta.validation.Valid;
import lk.ijse.posbackend.dto.ItemDTO;
import lk.ijse.posbackend.service.ItemService;
import lk.ijse.posbackend.util.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<APIResponse<String>> saveItem(
            @RequestBody @Valid ItemDTO dto) {

        itemService.saveItem(dto);

        return new ResponseEntity<>(
                new APIResponse<>(
                        201,
                        "Item saved successfully",
                        null
                ),
                HttpStatus.CREATED
        );
    }

    @PutMapping
    public ResponseEntity<APIResponse<String>> updateItem(
            @RequestBody @Valid ItemDTO dto) {

        itemService.updateItem(dto);

        return ResponseEntity.ok(
                new APIResponse<>(
                        200,
                        "Item updated successfully",
                        null
                )
        );
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<APIResponse<String>> deleteItem(
            @PathVariable String code) {

        itemService.deleteItem(code);

        return ResponseEntity.ok(
                new APIResponse<>(
                        200,
                        "Item deleted successfully",
                        null
                )
        );
    }

    @GetMapping
    public ResponseEntity<APIResponse<?>> getAllItems() {

        return ResponseEntity.ok(
                new APIResponse<>(
                        200,
                        "Success",
                        itemService.getAllItems()
                )
        );
    }
}