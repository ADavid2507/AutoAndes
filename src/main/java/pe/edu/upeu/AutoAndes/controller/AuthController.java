package pe.edu.upeu.AutoAndes.controller;

import pe.edu.upeu.AutoAndes.dto.*;
import pe.edu.upeu.AutoAndes.service.service.AuthService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/login")
    @SecurityRequirements
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO r) {
        return service.login(r);
    }

    @GetMapping("/me")
    public UsuarioResponseDTO me(Principal p) {
        return service.me(p.getName());
    }
}
