package pe.edu.upeu.AutoAndes.security;

import jakarta.servlet.http.*;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    private final RestSecurityError errors;

    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException {
        errors.escribir(request, response, HttpStatus.FORBIDDEN, "No tiene permiso para esta operación");
    }
}
