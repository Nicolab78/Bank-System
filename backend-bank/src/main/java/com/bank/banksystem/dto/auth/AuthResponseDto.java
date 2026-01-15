package com.bank.banksystem.dto.auth;

import com.bank.banksystem.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {

    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private UserDto user;

}