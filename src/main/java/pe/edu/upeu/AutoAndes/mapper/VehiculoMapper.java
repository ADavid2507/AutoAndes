package pe.edu.upeu.AutoAndes.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.AutoAndes.entity.*;
import pe.edu.upeu.AutoAndes.dto.*;
@Component public class VehiculoMapper {
    public Vehiculo toEntity(VehiculoRequestDTO r,Cliente c) { Vehiculo v=new Vehiculo(); actualizarEntidad(v,r,c); return v; }
    public void actualizarEntidad(Vehiculo v,VehiculoRequestDTO r,Cliente c) { v.setPlaca(r.getPlaca()); v.setMarca(r.getMarca().trim()); v.setModelo(r.getModelo().trim()); v.setAnio(r.getAnio()); v.setCliente(c); }
    public VehiculoResponseDTO toResponse(Vehiculo v) { return new VehiculoResponseDTO(v.getId(),v.getPlaca(),v.getMarca(),v.getModelo(),v.getAnio(),v.getCliente().getId(),v.getCliente().getNombres()); }
}
