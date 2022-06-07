package com.comercio.clientes.service.impl;

import com.comercio.clientes.domain.Direccion;
import com.comercio.clientes.repository.DireccionRepository;
import com.comercio.clientes.service.DireccionService;
import com.comercio.clientes.service.dto.DireccionDTO;
import com.comercio.clientes.service.mapper.DireccionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Direccion}.
 */
@Service
@Transactional
public class DireccionServiceImpl implements DireccionService {

    private final Logger log = LoggerFactory.getLogger(DireccionServiceImpl.class);

    private final DireccionRepository direccionRepository;

    private final DireccionMapper direccionMapper;

    public DireccionServiceImpl(DireccionRepository direccionRepository, DireccionMapper direccionMapper) {
        this.direccionRepository = direccionRepository;
        this.direccionMapper = direccionMapper;
    }

    @Override
    public DireccionDTO save(DireccionDTO direccionDTO) {
        log.debug("Request to save Direccion : {}", direccionDTO);
        Direccion direccion = direccionMapper.toEntity(direccionDTO);
        direccion = direccionRepository.save(direccion);
        return direccionMapper.toDto(direccion);
    }

    @Override
    public Optional<DireccionDTO> partialUpdate(DireccionDTO direccionDTO) {
        log.debug("Request to partially update Direccion : {}", direccionDTO);

        return direccionRepository
            .findById(direccionDTO.getId())
            .map(existingDireccion -> {
                direccionMapper.partialUpdate(existingDireccion, direccionDTO);

                return existingDireccion;
            })
            .map(direccionRepository::save)
            .map(direccionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DireccionDTO> findAll() {
        log.debug("Request to get all Direccions");
        return direccionRepository.findAll().stream().map(direccionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DireccionDTO> findOne(Long id) {
        log.debug("Request to get Direccion : {}", id);
        return direccionRepository.findById(id).map(direccionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Direccion : {}", id);
        direccionRepository.deleteById(id);
    }
}
