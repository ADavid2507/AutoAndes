package pe.edu.upeu.AutoAndes.security;

import jakarta.servlet.http.*;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.core.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final RestSecurityError errors;

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {
        errors.escribir(request, response, HttpStatus.UNAUTHORIZED, "Autenticación requerida o token inválido");
    }
}
