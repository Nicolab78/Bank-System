package com.bank.banksystem.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
public class UpdateUserDTO {
    private String username;
    private String email;
    private String password;
}
