package lk.ijse.posbackend.service.impl;

import lk.ijse.posbackend.dto.UserDTO;
import lk.ijse.posbackend.entity.User;
import lk.ijse.posbackend.exception.CustomException;
import lk.ijse.posbackend.repository.UserRepository;
import lk.ijse.posbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void login(UserDTO dto) {

        User user = userRepository
                .findById(dto.getUsername())
                .orElseThrow(() ->
                        new CustomException("User not found"));

        if (!user.getPassword().equals(dto.getPassword())) {
            throw new CustomException("Invalid password");
        }
    }
}
