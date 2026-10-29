package pe.edu.upeu.AutoAndes.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.AutoAndes.entity.Cliente;
import pe.edu.upeu.AutoAndes.dto.ClienteResponseDTO;
@Component public class ClienteMapper {
    public ClienteResponseDTO toResponse(Cliente c) { return new ClienteResponseDTO(c.getId(),c.getDocumento(),c.getNombres(),c.getTelefono()); }
}
