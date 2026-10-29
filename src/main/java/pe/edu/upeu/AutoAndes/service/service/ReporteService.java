package pe.edu.upeu.AutoAndes.service.service;

import pe.edu.upeu.AutoAndes.dto.IngresoServicioDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReporteService {
    List<IngresoServicioDTO> ingresos(LocalDate desde, LocalDate hasta);
}
