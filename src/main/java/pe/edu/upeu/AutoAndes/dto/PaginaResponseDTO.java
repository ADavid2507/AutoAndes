package pe.edu.upeu.AutoAndes.dto;

import lombok.*;
import org.springframework.data.domain.Page;
import java.util.List;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PaginaResponseDTO<T> {
    private List<T> contenido;
    private int pagina;
    private int tamanio;
    private long totalElementos;
    private int totalPaginas;
    private boolean ultima;
    public static <T> PaginaResponseDTO<T> desde(Page<T> page) {
        return new PaginaResponseDTO<>(page.getContent(),page.getNumber(),page.getSize(),page.getTotalElements(),page.getTotalPages(),page.isLast());
    }
}
