package pe.edu.upeu.AutoAndes.security;

import jakarta.servlet.http.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;
import pe.edu.upeu.AutoAndes.exception.dto.ErrorResponseDTO;

import java.io.IOException;
import java.time.*;

@Component
@RequiredArgsConstructor
public class RestSecurityError {
    private final ObjectMapper mapper;

    public void escribir(HttpServletRequest request, HttpServletResponse response, HttpStatus status, String mensaje) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().
                write(mapper.writeValueAsString(new ErrorResponseDTO(LocalDateTime.now(ZoneId.of("America/Lima")),
                        status.value(),
                        status.getReasonPhrase(),
                        mensaje,
                        request.getRequestURI(),
                        null)));
    }
}
