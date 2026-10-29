package pe.edu.upeu.AutoAndes.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.AutoAndes.entity.Servicio;
import pe.edu.upeu.AutoAndes.dto.*;
@Component public class ServicioMapper {
    public Servicio toEntity(ServicioRequestDTO r) { Servicio s=new Servicio(); actualizarEntidad(s,r); return s; }
    public void actualizarEntidad(Servicio s,ServicioRequestDTO r) { s.setCodigo(r.getCodigo()); s.setNombre(r.getNombre().trim()); s.setPrecio(r.getPrecio()); s.setDuracionMin(r.getDuracionMin()); s.setActivo(r.getActivo()); }
    public ServicioResponseDTO toResponse(Servicio s) { return new ServicioResponseDTO(s.getId(),s.getCodigo(),s.getNombre(),s.getPrecio(),s.getDuracionMin(),s.getActivo()); }
}
