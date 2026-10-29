package pe.edu.upeu.AutoAndes.controller;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import java.net.URI;
import java.util.List;

import pe.edu.upeu.AutoAndes.dto.*;
import pe.edu.upeu.AutoAndes.service.service.ServicioService;

@RestController
@RequestMapping("/api/v1/servicios")
@RequiredArgsConstructor
public class ServicioController {
    private final ServicioService service;

    @GetMapping
    @PreAuthorize("hasAuthority('SERVICIO_LEER')")
    public List<ServicioResponseDTO> listar() {
        return service.readAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SERVICIO_LEER')")
    public ServicioResponseDTO leer(@PathVariable Long id) {
        return service.read(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SERVICIO_ESCRIBIR')")
    public ResponseEntity<ServicioResponseDTO> crear(@Valid @RequestBody ServicioRequestDTO r) {
        var result = service.create(r);
        return ResponseEntity.created(URI.create("/api/v1/servicios/" + result.getId())).body(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SERVICIO_ESCRIBIR')")
    public ServicioResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody ServicioRequestDTO r) {
        return service.update(id, r);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SERVICIO_ESCRIBIR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAuthority('SERVICIO_ESCRIBIR')")
    public ServicioResponseDTO estado(@PathVariable Long id, @Valid @RequestBody EstadoServicioRequestDTO r) {
        return service.cambiarEstado(id, r);
    }

    @GetMapping("/paginado")
    @PreAuthorize("hasAuthority('SERVICIO_LEER')")
    public PaginaResponseDTO<ServicioResponseDTO> paginado(@RequestParam(defaultValue="0") int pagina,
            @RequestParam(defaultValue="10") int tamanio,
            @RequestParam(defaultValue="id") String ordenarPor,
            @RequestParam(defaultValue="asc") String direccion,
            @RequestParam(required=false) String buscar) {
        return service.buscarPaginado(pagina,tamanio,ordenarPor,direccion,buscar);
    }

}
