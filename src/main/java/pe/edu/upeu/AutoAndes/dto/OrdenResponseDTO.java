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
public class OrdenResponseDTO {
    private Long id;
    private String numero;
    private LocalDateTime fecha;
    private Long vehiculoId;
    private String placa;
    private String clienteNombre;
    private Integer kilometraje;
    private String estado;
    private BigDecimal total;
    private String usuarioRegistro;
    private String motivoAnulacion;
    private List<DetalleOrdenResponseDTO> detalles;
}
