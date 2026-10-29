package pe.edu.upeu.AutoAndes.service.impl;

import pe.edu.upeu.AutoAndes.dto.IngresoServicioDTO;
import pe.edu.upeu.AutoAndes.exception.SolicitudInvalidaException;
import pe.edu.upeu.AutoAndes.repository.DetalleOrdenRepository;
import pe.edu.upeu.AutoAndes.service.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReporteServiceImpl implements ReporteService {
    private final DetalleOrdenRepository detalles;

    public List<IngresoServicioDTO> ingresos(LocalDate desde, LocalDate hasta) {
        if (desde.isAfter(hasta)) throw new SolicitudInvalidaException("Desde no puede ser posterior a hasta");
        return detalles.ingresos(desde.atStartOfDay(), hasta.plusDays(1).atStartOfDay());
    }
}
