package com.bank.banksystem.controller;

import com.bank.banksystem.dto.auth.AuthResponseDto;
import com.bank.banksystem.dto.auth.LoginDto;
import com.bank.banksystem.dto.auth.RegisterDto;
import com.bank.banksystem.dto.user.UserDto;
import com.bank.banksystem.service.impl.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:4200"})
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {

        log.info("Tentative de connexion pour: {}", loginDto.getUsername());

        try {
            AuthResponseDto authResponse = authService.login(loginDto);

            return ResponseEntity.ok(authResponse);

        } catch (Exception e) {
            log.warn("Échec de la connexion pour {}: {}", loginDto.getUsername(), e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "success", false,
                            "message", "Identifiants incorrects"
                    ));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto registerDto) {

        log.info("Tentative d'inscription pour: {}", registerDto.getUsername());

        try {
            AuthResponseDto authResponse = authService.register(registerDto);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(authResponse);

        } catch (IllegalArgumentException e) {
            log.warn("Échec de l'inscription pour {}: {}", registerDto.getUsername(), e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "message", e.getMessage()
                    ));
        } catch (Exception e) {
            log.error("Erreur lors de l'inscription pour {}: {}", registerDto.getUsername(), e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Erreur interne du serveur"
                    ));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {

        String refreshToken = request.get("refreshToken");

        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "message", "Refresh token manquant"
                    ));
        }

        try {
            AuthResponseDto authResponse = authService.refreshToken(refreshToken);

            return ResponseEntity.ok(authResponse);

        } catch (Exception e) {
            log.warn("Échec du renouvellement de token: {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "success", false,
                            "message", "Token de rafraîchissement invalide"
                    ));
        }
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getProfile(Authentication authentication) {

        try {
            UserDto userDto = authService.getUserProfile(authentication.getName());

            return ResponseEntity.ok(userDto);

        } catch (Exception e) {
            log.error("Erreur lors de la récupération du profil pour {}: {}",
                    authentication.getName(), e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Erreur lors de la récupération du profil"
                    ));
        }
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logout(Authentication authentication) {

        log.info("Déconnexion de l'utilisateur: {}", authentication.getName());

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Déconnexion réussie"
        ));
    }

    @GetMapping("/verify")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> verifyToken(Authentication authentication) {

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Token valide",
                "username", authentication.getName()
        ));
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "Banking Authentication Service",
                "timestamp", System.currentTimeMillis()
        ));
    }
}