package pe.edu.upeu.AutoAndes.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdenRequestDTO {
    @NotNull
    @Positive
    private Long vehiculoId;
    @NotNull
    @Min(0)
    @Max(9999999)
    private Integer kilometraje;
    @NotEmpty
    @Size(min = 1, max = 10)
    @Valid
    private List<@NotNull DetalleOrdenRequestDTO> detalles;
}
