package pe.edu.upeu.AutoAndes.service.service;

import pe.edu.upeu.AutoAndes.dto.*;
import pe.edu.upeu.AutoAndes.enums.EstadoOrden;

import java.time.LocalDate;
import java.util.List;

public interface OrdenService {
    OrdenResponseDTO registrar(OrdenRequestDTO request, String username);

    OrdenResponseDTO read(Long id);

    OrdenResponseDTO anular(Long id, AnulacionRequestDTO request);

    List<OrdenResponseDTO> buscar(String placa, EstadoOrden estado, LocalDate desde, LocalDate hasta);
    PaginaResponseDTO<OrdenResponseDTO> buscarPaginado(int pagina,int tamanio,String ordenarPor,String direccion,String placa,EstadoOrden estado,LocalDate desde,LocalDate hasta);

}
