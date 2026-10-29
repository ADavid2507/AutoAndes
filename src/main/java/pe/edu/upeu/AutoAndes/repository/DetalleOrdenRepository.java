package pe.edu.upeu.AutoAndes.repository;

import pe.edu.upeu.AutoAndes.entity.*;
import pe.edu.upeu.AutoAndes.enums.EstadoOrden;
import pe.edu.upeu.AutoAndes.dto.IngresoServicioDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

import java.util.*;
import java.time.LocalDateTime;

public interface DetalleOrdenRepository extends JpaRepository<DetalleOrden, Long> {
    boolean existsByServicioId(Long id);

    @Query("""
        select new pe.edu.upeu.AutoAndes.dto.IngresoServicioDTO(
        d.servicio.codigo,
        d.servicio.nombre,
        sum(d.cantidad),
        sum(d.subtotal)) 
        from DetalleOrden d
         where d.orden.estado=pe.edu.upeu.AutoAndes.enums.EstadoOrden.REGISTRADA 
         and d.orden.fecha>=:desde 
         and d.orden.fecha<:hasta 
         group by d.servicio.codigo,d.servicio.nombre 
         order by sum(d.subtotal) desc,
         d.servicio.codigo asc
""")
    List<IngresoServicioDTO> ingresos(@Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta);
}
