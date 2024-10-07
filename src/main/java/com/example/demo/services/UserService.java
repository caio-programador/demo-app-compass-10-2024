package com.example.demo.services;

import com.example.demo.entities.User;
import com.example.demo.web.dtos.UserCreateDTO;
import com.example.demo.web.dtos.UserPasswordDTO;
import com.example.demo.web.dtos.UserResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface UserService {
    @Transactional
    UserResponseDTO createUser(UserCreateDTO userCreateDTO);
    @Transactional(readOnly=true)
    UserResponseDTO findById(Long id);
    @Transactional(readOnly=true)
    List<UserResponseDTO> findAll();
    @Transactional
    void changePassword(UserPasswordDTO user, Long id);
    void delete(Long id);
    @Transactional(readOnly=true)
    User findByUsername(String username);
}
