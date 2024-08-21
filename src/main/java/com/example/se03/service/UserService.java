package com.example.se03.service;

import com.example.se03.dto.UserDTO;

public interface UserService {
    int saveUser(UserDTO userDTO);
    UserDTO serachUser(String username);
}
