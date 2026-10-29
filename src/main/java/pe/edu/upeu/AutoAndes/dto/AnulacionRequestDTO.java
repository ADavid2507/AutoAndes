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
public class AnulacionRequestDTO {
    @NotBlank
    @Size(min = 10, max = 200)
    private String motivo;
}
