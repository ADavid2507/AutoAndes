package pe.edu.upeu.AutoAndes.exception;

import pe.edu.upeu.AutoAndes.exception.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.slf4j.*;

import java.time.*;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<ErrorResponseDTO> error(HttpStatus status, String mensaje, HttpServletRequest request, Map<String, String> campos) {
        if (status == HttpStatus.CONFLICT) LOG.warn("Regla rechazada en {}: {}", request.getRequestURI(), mensaje);
        return ResponseEntity.status(status).body(new ErrorResponseDTO(LocalDateTime.now(ZoneId.of("America/Lima")), status.value(), status.getReasonPhrase(), mensaje, request.getRequestURI(), campos));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponseDTO> validation(MethodArgumentNotValidException ex, HttpServletRequest r) {
        Map<String, String> campos = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> campos.putIfAbsent(e.getField(), e.getDefaultMessage()));
        return error(HttpStatus.BAD_REQUEST, "Datos inválidos", r, campos);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class, MissingServletRequestParameterException.class, SolicitudInvalidaException.class})
    ResponseEntity<ErrorResponseDTO> invalid(Exception ex, HttpServletRequest r) {
        if (ex instanceof SolicitudInvalidaException)
            LOG.warn("Regla rechazada en {}: {}", r.getRequestURI(), ex.getMessage());
        return error(HttpStatus.BAD_REQUEST, ex instanceof SolicitudInvalidaException ? ex.getMessage() : "Solicitud inválida; revise parámetros, tipos y fechas (yyyy-MM-dd)", r, null);
    }

    @ExceptionHandler({RecursoNoEncontradoException.class, NoResourceFoundException.class})
    ResponseEntity<ErrorResponseDTO> missing(Exception ex, HttpServletRequest r) {
        return error(HttpStatus.NOT_FOUND, ex instanceof RecursoNoEncontradoException ? ex.getMessage() : "Recurso no encontrado", r, null);
    }

    @ExceptionHandler(ReglaNegocioException.class)
    ResponseEntity<ErrorResponseDTO> conflict(Exception ex, HttpServletRequest r) {
        return error(HttpStatus.CONFLICT, ex.getMessage(), r, null);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, ConcurrencyFailureException.class})
    ResponseEntity<ErrorResponseDTO> integrity(Exception ex, HttpServletRequest r) {
        return error(HttpStatus.CONFLICT, "La operación entra en conflicto con datos existentes; vuelva a consultar", r, null);
    }

    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<ErrorResponseDTO> auth(Exception ex, HttpServletRequest r) {
        return error(HttpStatus.UNAUTHORIZED, "Credenciales inválidas", r, null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ErrorResponseDTO> denied(Exception ex, HttpServletRequest r) {
        return error(HttpStatus.FORBIDDEN, "No tiene permiso para esta operación", r, null);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponseDTO> unexpected(Exception ex, HttpServletRequest r) {
        String referencia = UUID.randomUUID().toString();
        LOG.error("Error inesperado referencia={} tipo={}", referencia, ex.getClass().getSimpleName());
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado. Referencia: " + referencia, r, null);
    }
}
