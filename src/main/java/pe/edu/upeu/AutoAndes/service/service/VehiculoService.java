package pe.edu.upeu.AutoAndes.service.service;

import java.util.List;

import pe.edu.upeu.AutoAndes.dto.*;
import pe.edu.upeu.AutoAndes.service.generic.CrudService;

public interface VehiculoService extends CrudService<VehiculoRequestDTO, VehiculoResponseDTO, Long> {
    List<VehiculoResponseDTO> readAll(Long clienteId);
}
