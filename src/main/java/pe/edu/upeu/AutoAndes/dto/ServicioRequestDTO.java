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
public class ServicioRequestDTO {
    @NotBlank(message = "El código es obligatorio")
    @Pattern(regexp = "SRV-[0-9]{3}", message = "El código debe tener el formato SRV-000")
    private String codigo;
    @NotBlank
    private String nombre;
    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    @Digits(integer = 8, fraction = 2)
    private BigDecimal precio;
    @NotNull
    @Min(15)
    @Max(480)
    private Integer duracionMin;
    @NotNull
    private Boolean activo;

    @Size(min = 3, max = 80)
    public String getNombre() {
        return nombre == null ? null : nombre.trim();
    }

    public void setNombre(String nombre) {
        this.nombre = nombre == null ? null : nombre.trim();
    }
}
