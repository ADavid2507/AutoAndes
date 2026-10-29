package pe.edu.upeu.AutoAndes.service.generic;

import org.springframework.data.domain.*;
import pe.edu.upeu.AutoAndes.exception.SolicitudInvalidaException;

import java.util.Map;

public final class Paginacion {
    private Paginacion() {
    }

    public static Pageable crear(int pagina, int tamanio, String ordenarPor, String direccion, Map<String, String> campos) {
        if (pagina < 0) throw new SolicitudInvalidaException("La página no puede ser negativa");
        if (tamanio < 1 || tamanio > 100) throw new SolicitudInvalidaException("El tamaño debe estar entre 1 y 100");
        String campo = campos.get(ordenarPor);
        if (campo == null) throw new SolicitudInvalidaException("Campo de ordenamiento no permitido");
        if (!"asc".equalsIgnoreCase(direccion) && !"desc".equalsIgnoreCase(direccion))
            throw new SolicitudInvalidaException("La dirección debe ser asc o desc");
        Sort sort = Sort.by(Sort.Direction.fromString(direccion), campo);
        if (!campo.equals("id")) sort = sort.and(Sort.by(Sort.Direction.fromString(direccion), "id"));
        return PageRequest.of(pagina, tamanio, sort);
    }
}
