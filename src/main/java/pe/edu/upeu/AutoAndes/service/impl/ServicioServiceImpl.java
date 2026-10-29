package pe.edu.upeu.AutoAndes.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.util.List;

import pe.edu.upeu.AutoAndes.dto.*;
import pe.edu.upeu.AutoAndes.entity.*;
import pe.edu.upeu.AutoAndes.exception.*;
import pe.edu.upeu.AutoAndes.repository.*;
import pe.edu.upeu.AutoAndes.mapper.ServicioMapper;
import pe.edu.upeu.AutoAndes.service.service.ServicioService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ServicioServiceImpl implements ServicioService {
    private static final Logger LOG = LoggerFactory.getLogger(ServicioServiceImpl.class);
    private final ServicioRepository repository;
    private final DetalleOrdenRepository referencias;
    private final ServicioMapper mapper;

    private Servicio buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado"));
    }

    @Override
    public List<ServicioResponseDTO> readAll() {
        return repository.findAll(Sort.by("id")).stream().map(mapper::toResponse).toList();
    }

    @Override
    public ServicioResponseDTO read(Long id) {
        return mapper.toResponse(buscar(id));
    }

    @Override
    @Transactional
    public ServicioResponseDTO create(ServicioRequestDTO r) {
        if (repository.existsByCodigoIgnoreCase(r.getCodigo())) throw new ReglaNegocioException("Codigo ya existe");

        Servicio value = repository.saveAndFlush(mapper.toEntity(r));
        LOG.info("Servicio creado id={}", value.getId());
        return mapper.toResponse(value);
    }

    @Override
    @Transactional
    public ServicioResponseDTO update(Long id, ServicioRequestDTO r) {
        Servicio value = buscar(id);
        if (repository.existsByCodigoIgnoreCaseAndIdNot(r.getCodigo(), id))
            throw new ReglaNegocioException("Codigo ya existe");

        mapper.actualizarEntidad(value, r);
        repository.flush();
        LOG.info("Servicio actualizado id={}", id);
        return mapper.toResponse(value);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Servicio value = buscar(id);
        if (referencias.existsByServicioId(id))
            throw new ReglaNegocioException("Servicio usado en órdenes; no se puede eliminar físicamente. Use PATCH /servicios/{id}/estado");
        repository.delete(value);
        repository.flush();
        LOG.info("Servicio eliminado id={}", id);
    }

    @Override
    @Transactional
    public ServicioResponseDTO cambiarEstado(Long id, EstadoServicioRequestDTO r) {
        Servicio s = buscar(id);
        s.setActivo(r.getActivo());
        LOG.info("Estado de servicio actualizado id={}", id);
        return mapper.toResponse(s);
    }
}
