package pe.edu.upeu.AutoAndes.service.service;

import java.util.List;

import pe.edu.upeu.AutoAndes.dto.ClienteResponseDTO;

public interface ClienteService {
    List<ClienteResponseDTO> readAll();
}
