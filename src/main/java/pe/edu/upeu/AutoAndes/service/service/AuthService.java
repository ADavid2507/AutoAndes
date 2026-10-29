package pe.edu.upeu.AutoAndes.service.service;

import pe.edu.upeu.AutoAndes.dto.*;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO r);

    UsuarioResponseDTO me(String username);
}
