package pe.edu.upeu.AutoAndes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

import pe.edu.upeu.AutoAndes.dto.ClienteResponseDTO;
import pe.edu.upeu.AutoAndes.service.service.ClienteService;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {
    private final ClienteService service;

    @GetMapping
    @PreAuthorize("hasAuthority('VEHICULO_LEER')")
    public List<ClienteResponseDTO> listar() {
        return service.readAll();
    }
}
