package com.taskmanager.taskmanager.user;
import com.taskmanager.taskmanager.dto.UserRequest;
import com.taskmanager.taskmanager.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user->new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail()
                )).toList();
    }
    public UserResponse createUser(UserRequest request) {

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );
    }

    public UserResponse updateUser(UserRequest request,Long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(
                    passwordEncoder.encode(request.getPassword())
            );
        }
        User updateduser =  userRepository.save(user);
        return new UserResponse(updateduser.getId(),updateduser.getName(),updateduser.getEmail());
    }
    public UserResponse getUserById(Long id) {
        User user =  userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
        userRepository.delete(user);
    }
}
