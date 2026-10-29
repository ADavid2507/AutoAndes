package pe.edu.upeu.AutoAndes.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.AutoAndes.entity.*;
import pe.edu.upeu.AutoAndes.dto.*;
@Component public class OrdenMapper {
    public OrdenResponseDTO toResponse(OrdenServicio o) {
        return new OrdenResponseDTO(o.getId(),o.getNumero(),o.getFecha(),o.getVehiculo().getId(),o.getVehiculo().getPlaca(),o.getVehiculo().getCliente().getNombres(),o.getKilometraje(),o.getEstado().name(),o.getTotal(),o.getUsuarioRegistro(),o.getMotivoAnulacion(),
            o.getDetalles().stream().map(d -> new DetalleOrdenResponseDTO(d.getId(),d.getServicio().getId(),d.getServicio().getCodigo(),d.getServicio().getNombre(),d.getCantidad(),d.getPrecioUnitario(),d.getSubtotal())).toList());
    }
}
