package lk.ijse.posbackend.controller;

import jakarta.validation.Valid;
import lk.ijse.posbackend.dto.UserDTO;
import lk.ijse.posbackend.service.UserService;
import lk.ijse.posbackend.util.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<APIResponse<String>> login(
            @RequestBody @Valid UserDTO dto) {

        userService.login(dto);

        return new ResponseEntity<>(
                new APIResponse<>(
                        200,
                        "Login successful",
                        null
                ),
                HttpStatus.OK
        );
    }
}
