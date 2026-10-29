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
public class DetalleOrdenRequestDTO {
    @NotNull
    @Positive
    private Long servicioId;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer cantidad;
}
