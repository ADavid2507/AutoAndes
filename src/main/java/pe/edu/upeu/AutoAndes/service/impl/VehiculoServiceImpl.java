package pe.edu.upeu.AutoAndes.service.impl;

import pe.edu.upeu.AutoAndes.service.generic.Paginacion;
import org.springframework.data.jpa.domain.Specification;
import java.util.Map;
import java.util.Locale;

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
import pe.edu.upeu.AutoAndes.mapper.VehiculoMapper;
import pe.edu.upeu.AutoAndes.service.service.VehiculoService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VehiculoServiceImpl implements VehiculoService {
    private static final Logger LOG = LoggerFactory.getLogger(VehiculoServiceImpl.class);
    private final VehiculoRepository repository;
    private final OrdenServicioRepository referencias;
    private final VehiculoMapper mapper;
    private final ClienteRepository clientes;

    private Vehiculo buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Vehiculo no encontrado"));
    }

    @Override
    public List<VehiculoResponseDTO> readAll() {
        return readAll(null);
    }

    @Override
    public List<VehiculoResponseDTO> readAll(Long clienteId) {
        return (clienteId == null ? repository.findAll(Sort.by("id")) : repository.findByClienteIdOrderByIdAsc(clienteId)).stream().map(mapper::toResponse).toList();
    }

    @Override
    public VehiculoResponseDTO read(Long id) {
        return mapper.toResponse(buscar(id));
    }

    @Override
    @Transactional
    public VehiculoResponseDTO create(VehiculoRequestDTO r) {
        if (repository.existsByPlacaIgnoreCase(r.getPlaca())) throw new ReglaNegocioException("Placa ya existe");
        Cliente cliente = clientes.findById(r.getClienteId()).orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));
        Vehiculo value = repository.saveAndFlush(mapper.toEntity(r, cliente));
        LOG.info("Vehiculo creado id={}", value.getId());
        return mapper.toResponse(value);
    }

    @Override
    @Transactional
    public VehiculoResponseDTO update(Long id, VehiculoRequestDTO r) {
        Vehiculo value = buscar(id);
        if (repository.existsByPlacaIgnoreCaseAndIdNot(r.getPlaca(), id))
            throw new ReglaNegocioException("Placa ya existe");
        Cliente cliente = clientes.findById(r.getClienteId()).orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));
        mapper.actualizarEntidad(value, r, cliente);
        repository.flush();
        LOG.info("Vehiculo actualizado id={}", id);
        return mapper.toResponse(value);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Vehiculo value = buscar(id);
        if (referencias.existsByVehiculoId(id))
            throw new ReglaNegocioException("Vehiculo usado en órdenes; no se puede eliminar");
        repository.delete(value);
        repository.flush();
        LOG.info("Vehiculo eliminado id={}", id);
    }


    @Override
    public PaginaResponseDTO<VehiculoResponseDTO> buscarPaginado(int pagina,int tamanio,String ordenarPor,String direccion,String buscar,Long clienteId) {
        var pageable=Paginacion.crear(pagina,tamanio,ordenarPor,direccion,Map.of("placa","placa","marca","marca","modelo","modelo","anio","anio","clienteNombre","cliente.nombres","id","id"));
        Specification<Vehiculo> spec=(root,query,cb)->{
            var condiciones=new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();
            if(buscar!=null&&!buscar.isBlank()) {
                String patron="%"+buscar.trim().toLowerCase(Locale.ROOT).replace("\\","\\\\").replace("%","\\%").replace("_","\\_")+"%";
                condiciones.add(cb.or(cb.like(cb.lower(root.get("placa")),patron,'\\'),cb.like(cb.lower(root.get("marca")),patron,'\\'),cb.like(cb.lower(root.get("modelo")),patron,'\\'),cb.like(cb.lower(root.get("cliente").get("nombres")),patron,'\\')));
            }
            if(clienteId!=null) condiciones.add(cb.equal(root.get("cliente").get("id"),clienteId));
            return cb.and(condiciones.toArray(jakarta.persistence.criteria.Predicate[]::new));
        };
        return PaginaResponseDTO.desde(repository.findAll(spec,pageable).map(mapper::toResponse));
    }

}
