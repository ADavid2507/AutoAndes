package pe.edu.upeu.AutoAndes.service.impl;

import pe.edu.upeu.AutoAndes.dto.*;
import pe.edu.upeu.AutoAndes.entity.Usuario;
import pe.edu.upeu.AutoAndes.repository.UsuarioRepository;
import pe.edu.upeu.AutoAndes.service.service.AuthService;
import pe.edu.upeu.AutoAndes.security.*;
import pe.edu.upeu.AutoAndes.config.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {
    private final UsuarioRepository usuarios;
    private final UsuarioDetailsService details;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    private static final String DUMMY = "$2a$10$NNNgOCCajIjVDGhg3u7sHOwLlLrVMdIALH/64AmPlFmmpiG1yinGa";

    public LoginResponseDTO login(LoginRequestDTO r) {
        if (r.getPassword().getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72)
            throw new BadCredentialsException("Credenciales inválidas");
        var encontrado = usuarios.findByUsername(r.getUsername());
        boolean coincide = encoder.matches(r.getPassword(), encontrado.map(Usuario::getPassword).orElse(DUMMY));
        if (encontrado.isEmpty() || !coincide || !Boolean.TRUE.equals(encontrado.get().getActivo()))
            throw new BadCredentialsException("Credenciales inválidas");
        var perfil = perfil(encontrado.get());
        return new LoginResponseDTO(jwt.generar(details.loadUserByUsername(perfil.getUsername())), "Bearer", 3600L, perfil.getUsername(), perfil.getNombreCompleto(), perfil.getRoles(), perfil.getPermisos());
    }

    public UsuarioResponseDTO me(String username) {
        return perfil(usuarios.findByUsername(username).orElseThrow(() -> new BadCredentialsException("Credenciales inválidas")));
    }

    private UsuarioResponseDTO perfil(Usuario u) {
        Set<String> roles = u.getRoles().stream().map(r -> r.getNombre()).collect(Collectors.toCollection(TreeSet::new));
        Set<String> permisos = roles.stream().flatMap(r -> PermisosPorRol.de(r).stream()).map(Enum::name).collect(Collectors.toCollection(TreeSet::new));
        return new UsuarioResponseDTO(u.getUsername(), u.getNombreCompleto(), roles, permisos);
    }
}
