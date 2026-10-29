package pe.edu.upeu.AutoAndes.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.util.List;

import pe.edu.upeu.AutoAndes.repository.ClienteRepository;
import pe.edu.upeu.AutoAndes.mapper.ClienteMapper;
import pe.edu.upeu.AutoAndes.dto.ClienteResponseDTO;
import pe.edu.upeu.AutoAndes.service.service.ClienteService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClienteServiceImpl implements ClienteService {
    private final ClienteRepository repository;
    private final ClienteMapper mapper;

    public List<ClienteResponseDTO> readAll() {
        return repository.findAll(Sort.by("id")).stream().map(mapper::toResponse).toList();
    }
}
