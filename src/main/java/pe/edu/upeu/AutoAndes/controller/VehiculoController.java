package pe.edu.upeu.AutoAndes.controller;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import java.net.URI;
import java.util.List;

import pe.edu.upeu.AutoAndes.dto.*;
import pe.edu.upeu.AutoAndes.service.service.VehiculoService;

@RestController
@RequestMapping("/api/v1/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {
    private final VehiculoService service;

    @GetMapping
    @PreAuthorize("hasAuthority('VEHICULO_LEER')")
    public List<VehiculoResponseDTO> listar(@RequestParam(required = false) Long clienteId) {
        return service.readAll(clienteId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VEHICULO_LEER')")
    public VehiculoResponseDTO leer(@PathVariable Long id) {
        return service.read(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('VEHICULO_ESCRIBIR')")
    public ResponseEntity<VehiculoResponseDTO> crear(@Valid @RequestBody VehiculoRequestDTO r) {
        var result = service.create(r);
        return ResponseEntity.created(URI.create("/api/v1/vehiculos/" + result.getId())).body(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('VEHICULO_ESCRIBIR')")
    public VehiculoResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody VehiculoRequestDTO r) {
        return service.update(id, r);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('VEHICULO_ESCRIBIR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/paginado")
    @PreAuthorize("hasAuthority('VEHICULO_LEER')")
    public PaginaResponseDTO<VehiculoResponseDTO> paginado(@RequestParam(defaultValue="0") int pagina,
            @RequestParam(defaultValue="10") int tamanio,
            @RequestParam(defaultValue="id") String ordenarPor,
            @RequestParam(defaultValue="asc") String direccion,
            @RequestParam(required=false) String buscar,
            @RequestParam(required=false) Long clienteId) {
        return service.buscarPaginado(pagina,tamanio,ordenarPor,direccion,buscar,clienteId);
    }

}
