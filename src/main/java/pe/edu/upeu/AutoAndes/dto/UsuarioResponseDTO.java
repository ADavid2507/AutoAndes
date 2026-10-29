package pe.edu.upeu.AutoAndes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {
    private String username;
    private String nombreCompleto;
    private Set<String> roles;
    private Set<String> permisos;
}
