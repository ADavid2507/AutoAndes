package pe.edu.upeu.AutoAndes.repository;

import pe.edu.upeu.AutoAndes.entity.*;
import pe.edu.upeu.AutoAndes.enums.EstadoOrden;
import pe.edu.upeu.AutoAndes.dto.IngresoServicioDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

import java.util.*;
import java.time.LocalDateTime;

public interface OrdenServicioRepository extends JpaRepository<OrdenServicio, Long>, JpaSpecificationExecutor<OrdenServicio> {
    boolean existsByVehiculoId(Long id);

    Optional<OrdenServicio> findFirstByVehiculoIdAndEstadoOrderByFechaDescIdDesc(Long id, EstadoOrden estado);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from OrdenServicio o where o.id=:id")
    Optional<OrdenServicio> bloquear(@Param("id") Long id);
}
