package pe.edu.upeu.AutoAndes.service.service;

import pe.edu.upeu.AutoAndes.dto.*;
import pe.edu.upeu.AutoAndes.service.generic.CrudService;

public interface ServicioService extends CrudService<ServicioRequestDTO, ServicioResponseDTO, Long> {
    ServicioResponseDTO cambiarEstado(Long id, EstadoServicioRequestDTO request);
}
