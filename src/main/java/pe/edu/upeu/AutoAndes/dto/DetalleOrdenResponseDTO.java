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
public class DetalleOrdenResponseDTO {
    private Long id;
    private Long servicioId;
    private String codigo;
    private String servicio;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
