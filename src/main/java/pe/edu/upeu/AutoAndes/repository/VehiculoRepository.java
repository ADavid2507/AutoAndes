package pe.edu.upeu.AutoAndes.repository;

import pe.edu.upeu.AutoAndes.entity.*;
import pe.edu.upeu.AutoAndes.enums.EstadoOrden;
import pe.edu.upeu.AutoAndes.dto.IngresoServicioDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

import java.util.*;
import java.time.LocalDateTime;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    boolean existsByPlacaIgnoreCase(String placa);

    boolean existsByPlacaIgnoreCaseAndIdNot(String placa, Long id);

    @EntityGraph(attributePaths = "cliente")
    List<Vehiculo> findByClienteIdOrderByIdAsc(Long clienteId);

    @Override
    @EntityGraph(attributePaths = "cliente")
    List<Vehiculo> findAll(org.springframework.data.domain.Sort sort);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select v from Vehiculo v where v.id=:id")
    Optional<Vehiculo> bloquear(@Param("id") Long id);
}
