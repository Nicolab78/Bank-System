package com.bank.banksystem.service.interfaces;

import com.bank.banksystem.dto.user.CreateUserDTO;
import com.bank.banksystem.dto.user.UpdateUserDTO;
import com.bank.banksystem.dto.user.UserDto;

import java.util.List;

public interface IUserService {
    List<UserDto> getAllUsers();

    UserDto createUser(CreateUserDTO createUserDTO);
    UserDto getUserById(Long id);
    UserDto getUserByUsername(String username);
    UserDto updateUser(Long id, UpdateUserDTO dto);
    void deleteUser(Long id);
}
