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
public class VehiculoRequestDTO {
    @NotBlank
    @Pattern(regexp = "[A-Z]{3}-[0-9]{3}", message = "La placa debe tener el formato ABC-123")
    private String placa;

    @NotBlank
    @Size(max = 40)
    private String marca;

    @NotBlank
    @Size(max = 40)
    private String modelo;

    @NotNull
    @Min(1990)
    @Max(2027)
    private Integer anio;

    @NotNull
    @Positive
    private Long clienteId;
}
