package pe.edu.upeu.AutoAndes.controller;

import pe.edu.upeu.AutoAndes.dto.*;
import pe.edu.upeu.AutoAndes.enums.EstadoOrden;
import pe.edu.upeu.AutoAndes.service.service.OrdenService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.time.LocalDate;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ordenes")
@RequiredArgsConstructor
public class OrdenController {
    private final OrdenService service;

    @PostMapping
    @PreAuthorize("hasAuthority('ORDEN_REGISTRAR')")
    public ResponseEntity<OrdenResponseDTO> registrar(@Valid @RequestBody OrdenRequestDTO r, Principal principal) {
        var orden = service.registrar(r, principal.getName());
        return ResponseEntity.created(URI.create("/api/v1/ordenes/" + orden.getId())).body(orden);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ORDEN_LEER')")
    public OrdenResponseDTO leer(@PathVariable Long id) {
        return service.read(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ORDEN_LEER')")
    public List<OrdenResponseDTO> buscar(@RequestParam(required = false) String placa, @RequestParam(required = false) EstadoOrden estado,
                                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
                                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return service.buscar(placa, estado, desde, hasta);
    }

    @PatchMapping("/{id}/anular")
    @PreAuthorize("hasAuthority('ORDEN_ANULAR')")
    public OrdenResponseDTO anular(@PathVariable Long id, @Valid @RequestBody AnulacionRequestDTO r) {
        return service.anular(id, r);
    }
}
