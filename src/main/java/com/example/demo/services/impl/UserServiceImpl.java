package com.example.demo.services.impl;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import com.example.demo.web.dtos.UserCreateDTO;
import com.example.demo.web.dtos.UserPasswordDTO;
import com.example.demo.web.dtos.UserResponseDTO;
import com.example.demo.web.exceptions.EntityNotFoundException;
import com.example.demo.web.exceptions.InvalidPasswordException;
import com.example.demo.web.exceptions.UniqueFieldViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO createUser(UserCreateDTO userCreateDTO) {
        try{
            User user = new User();
            user.setUsername(userCreateDTO.username());
            user.setPassword(passwordEncoder.encode(userCreateDTO.password()));
            user.setCreatedAt(LocalDateTime.now());
            user.setCreatedBy("Nobody");
            user.setRole(Role.ROLE_CLIENT);
            user = userRepository.save(user);
            return new UserResponseDTO(user.getId(),
                    user.getUsername(), user.getRole().name().substring("ROLE_".length()));
        }catch (DataIntegrityViolationException e){
            throw new UniqueFieldViolationException("User with this username alrady exists");
        }
    }

    @Override
    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("There is no User with this id"));
        return new UserResponseDTO(user.getId(),
                user.getUsername(), user.getRole().name().substring("ROLE_".length()));
    }

    @Override
    public List<UserResponseDTO> findAll() {
        List<User> users = userRepository.findAll();

        return users.stream().map((x) -> new UserResponseDTO(
                x.getId(),
                x.getUsername(),
                x.getRole().name().substring("ROLE_".length())

        )).toList();
    }

    @Override
    public void changePassword(UserPasswordDTO user, Long id) {
        User updatedUser = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("There is no User with this id"));
        if (!passwordEncoder.matches(user.password(), updatedUser.getPassword())) {
            throw new InvalidPasswordException("Passwords doesn't match");
        }
        updatedUser.setPassword(passwordEncoder.encode(user.newPassword()));
    }

    @Override
    public void delete(Long id) {
        User deleteUser = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("There is no User with this id"));

        userRepository.delete(deleteUser);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException("There is no User with this username"));
    }
}
