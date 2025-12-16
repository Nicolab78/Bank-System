package com.bank.banksystem.service.interfaces;

import com.bank.banksystem.dto.user.CreateUserDTO;
import com.bank.banksystem.dto.user.UpdateUserDTO;
import com.bank.banksystem.dto.user.UserDTO;

import java.util.List;

public interface IUserService {
    List<UserDTO> getAllUsers();

    UserDTO createUser(CreateUserDTO createUserDTO);
    UserDTO getUserById(Long id);
    UserDTO getUserByUsername(String username);
    UserDTO updateUser(Long id, UpdateUserDTO dto);
    void deleteUser(Long id);
}
