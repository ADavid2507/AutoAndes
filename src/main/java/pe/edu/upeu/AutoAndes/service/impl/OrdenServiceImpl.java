package pe.edu.upeu.AutoAndes.service.impl;

import pe.edu.upeu.AutoAndes.dto.*;
import pe.edu.upeu.AutoAndes.entity.*;
import pe.edu.upeu.AutoAndes.enums.EstadoOrden;
import pe.edu.upeu.AutoAndes.exception.*;
import pe.edu.upeu.AutoAndes.repository.*;
import pe.edu.upeu.AutoAndes.mapper.OrdenMapper;
import pe.edu.upeu.AutoAndes.service.service.OrdenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Sort;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;

import java.time.*;
import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrdenServiceImpl implements OrdenService {
    private static final Logger LOG = LoggerFactory.getLogger(OrdenServiceImpl.class);
    private final OrdenServicioRepository ordenes;
    private final VehiculoRepository vehiculos;
    private final ServicioRepository servicios;
    private final NumeroOrdenRepository numeros;
    private final OrdenMapper mapper;
    private final Clock reloj;

    @Transactional
    public OrdenResponseDTO registrar(OrdenRequestDTO r, String username) {
        // Lock per vehicle: simultaneous registrations cannot both pass RN-04 with stale mileage.
        Vehiculo vehiculo = vehiculos.bloquear(r.getVehiculoId()).orElseThrow(() -> new RecursoNoEncontradoException("Vehículo no encontrado"));
        Set<Long> ids = new HashSet<>();
        for (DetalleOrdenRequestDTO d : r.getDetalles()) {
            if (!ids.add(d.getServicioId()))
                throw new SolicitudInvalidaException("RN-03: No repita un servicio en los detalles");
        }
        ordenes.findFirstByVehiculoIdAndEstadoOrderByFechaDescIdDesc(vehiculo.getId(), EstadoOrden.REGISTRADA).ifPresent(ultima -> {
            if (r.getKilometraje() < ultima.getKilometraje())
                throw new ReglaNegocioException("RN-04: El último kilometraje registrado es " + ultima.getKilometraje());
        });
        OrdenServicio orden = new OrdenServicio();
        long numero = numeros.siguiente();
        if (numero > 999999) throw new ReglaNegocioException("Se agotó el correlativo OS de seis dígitos");
        orden.setNumero(String.format(Locale.ROOT, "OS-%06d", numero));
        orden.setVehiculo(vehiculo);
        orden.setFecha(LocalDateTime.now(reloj));
        orden.setKilometraje(r.getKilometraje());
        orden.setEstado(EstadoOrden.REGISTRADA);
        orden.setUsuarioRegistro(username);
        BigDecimal total = BigDecimal.ZERO;
        for (DetalleOrdenRequestDTO linea : r.getDetalles()) {
            Servicio servicio = servicios.findById(linea.getServicioId()).orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado: " + linea.getServicioId()));
            if (!Boolean.TRUE.equals(servicio.getActivo()))
                throw new ReglaNegocioException("RN-03: El servicio " + servicio.getCodigo() + " está inactivo");
            DetalleOrden detalle = new DetalleOrden();
            detalle.setOrden(orden);
            detalle.setServicio(servicio);
            detalle.setCantidad(linea.getCantidad());
            detalle.setPrecioUnitario(servicio.getPrecio());
            detalle.setSubtotal(servicio.getPrecio().multiply(BigDecimal.valueOf(linea.getCantidad())));
            orden.getDetalles().add(detalle);
            total = total.add(detalle.getSubtotal());
        }
        if (total.precision() - total.scale() > 8)
            throw new ReglaNegocioException("El total excede la capacidad monetaria del esquema");
        orden.setTotal(total);
        ordenes.saveAndFlush(orden);
        LOG.info("Orden registrada numero={}", orden.getNumero());
        return mapper.toResponse(orden);
    }

    public OrdenResponseDTO read(Long id) {
        return mapper.toResponse(ordenes.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Orden no encontrada")));
    }

    @Transactional
    public OrdenResponseDTO anular(Long id, AnulacionRequestDTO r) {
        OrdenServicio orden = ordenes.bloquear(id).orElseThrow(() -> new RecursoNoEncontradoException("Orden no encontrada"));
        if (orden.getEstado() != EstadoOrden.REGISTRADA)
            throw new ReglaNegocioException("RN-05: Solo se anula una orden REGISTRADA");
        if (!orden.getFecha().toLocalDate().equals(LocalDate.now(reloj)))
            throw new ReglaNegocioException("RN-05: Solo se anula el mismo día del registro");
        if (r.getMotivo().trim().length() < 10)
            throw new SolicitudInvalidaException("El motivo debe tener entre 10 y 200 caracteres útiles");
        orden.setEstado(EstadoOrden.ANULADA);
        orden.setMotivoAnulacion(r.getMotivo().trim());
        LOG.info("Orden anulada numero={}", orden.getNumero());
        return mapper.toResponse(orden);
    }

    public List<OrdenResponseDTO> buscar(String placa, EstadoOrden estado, LocalDate desde, LocalDate hasta) {
        if (desde != null && hasta != null && desde.isAfter(hasta))
            throw new SolicitudInvalidaException("Desde no puede ser posterior a hasta");
        Specification<OrdenServicio> spec = (root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> filtros = new ArrayList<>();
            if (placa != null && !placa.isBlank())
                filtros.add(cb.equal(cb.upper(root.get("vehiculo").get("placa")), placa.trim().toUpperCase(Locale.ROOT)));
            if (estado != null) filtros.add(cb.equal(root.get("estado"), estado));
            if (desde != null) filtros.add(cb.greaterThanOrEqualTo(root.get("fecha"), desde.atStartOfDay()));
            if (hasta != null) filtros.add(cb.lessThan(root.get("fecha"), hasta.plusDays(1).atStartOfDay()));
            return cb.and(filtros.toArray(jakarta.persistence.criteria.Predicate[]::new));
        };
        return ordenes.findAll(spec, Sort.by(Sort.Direction.DESC, "fecha", "id")).stream().map(mapper::toResponse).toList();
    }
}
