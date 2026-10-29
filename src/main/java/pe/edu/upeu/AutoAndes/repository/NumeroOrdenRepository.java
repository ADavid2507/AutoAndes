package pe.edu.upeu.AutoAndes.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NumeroOrdenRepository {
    private final EntityManager entityManager;

    public long siguiente() {
        return ((Number) entityManager.createNativeQuery("select orden_numero_seq.nextval from dual", Long.class).getSingleResult()).longValue();
    }
}
