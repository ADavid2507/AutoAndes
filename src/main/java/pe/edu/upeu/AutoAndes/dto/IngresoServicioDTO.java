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
public class IngresoServicioDTO {
    private String codigo;
    private String servicio;
    private Long cantidadTotal;
    private BigDecimal importeTotal;
}
