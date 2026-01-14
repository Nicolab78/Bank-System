package com.bank.banksystem.service.impl;

import com.bank.banksystem.dto.auth.AuthResponseDto;
import com.bank.banksystem.dto.auth.LoginDto;
import com.bank.banksystem.dto.auth.RegisterDto;
import com.bank.banksystem.dto.user.UserDto;
import com.bank.banksystem.model.Role;
import com.bank.banksystem.model.User;
import com.bank.banksystem.repository.UserRepository;
import com.bank.banksystem.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDto login(LoginDto loginDto) {

        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Identifiants incorrects"));

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String accessToken = jwtService.generateToken(userDetails);
            String refreshToken = jwtService.generateToken(userDetails);

            log.info("Connexion réussie pour l'utilisateur: {}", user.getUsername());

            UserDto userDto = mapToUserDto(user);

            return new AuthResponseDto(accessToken, refreshToken, jwtService.getExpirationTime(), userDto);

        } catch (BadCredentialsException e) {
            log.warn("Tentative de connexion échouée pour: {}", loginDto.getUsername());
            throw new BadCredentialsException("Identifiants incorrects");

        } catch (DisabledException e) {
            throw new DisabledException("Compte désactivé");
        }
    }

    public AuthResponseDto register(RegisterDto registerDto) {

        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new IllegalArgumentException("Ce nom d'utilisateur est déjà utilisé");
        }

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new IllegalArgumentException("Cet email est déjà utilisé");
        }

        User user = User.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .role(Role.USER)
                .enabled(true)
                .build();

        userRepository.save(user);

        log.info("Nouvel utilisateur créé: {}", user.getUsername());

        return login(new LoginDto(registerDto.getUsername(), registerDto.getPassword()));
    }

    public AuthResponseDto refreshToken(String refreshToken) {

        String username = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        org.springframework.security.core.userdetails.User tempUserDetails =
                (org.springframework.security.core.userdetails.User) org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .authorities("ROLE_" + user.getRole().name())
                        .build();

        if (!jwtService.isTokenValid(refreshToken, tempUserDetails)) {
            throw new IllegalArgumentException("Token de rafraîchissement expiré ou invalide");
        }

        String newAccessToken = jwtService.generateToken(tempUserDetails);
        String newRefreshToken = jwtService.generateToken(tempUserDetails);

        UserDto userDto = mapToUserDto(user);

        return new AuthResponseDto(newAccessToken, newRefreshToken, jwtService.getExpirationTime(), userDto);
    }

    public UserDto getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        return mapToUserDto(user);
    }

    private UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .enabled(user.isEnabled())
                .build();
    }
}