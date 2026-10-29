package pe.edu.upeu.AutoAndes.repository;

import pe.edu.upeu.AutoAndes.entity.*;
import pe.edu.upeu.AutoAndes.enums.EstadoOrden;
import pe.edu.upeu.AutoAndes.dto.IngresoServicioDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

import java.util.*;
import java.time.LocalDateTime;

public interface RolRepository extends JpaRepository<Rol, Long> {
}
