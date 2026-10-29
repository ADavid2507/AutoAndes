package pe.edu.upeu.AutoAndes.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import pe.edu.upeu.AutoAndes.enums.EstadoOrden;

@Entity
@Table(name="cliente")
@Getter
@Setter
@NoArgsConstructor
public class Cliente {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=11)
    private String documento;

    @Column(nullable=false, length=120)
    private String nombres;

    @Column(length=15)
    private String telefono;
}
