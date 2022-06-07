package com.comercio.pedidos.service.impl;

import com.comercio.pedidos.domain.Transportador;
import com.comercio.pedidos.repository.TransportadorRepository;
import com.comercio.pedidos.service.TransportadorService;
import com.comercio.pedidos.service.dto.TransportadorDTO;
import com.comercio.pedidos.service.mapper.TransportadorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Transportador}.
 */
@Service
@Transactional
public class TransportadorServiceImpl implements TransportadorService {

    private final Logger log = LoggerFactory.getLogger(TransportadorServiceImpl.class);

    private final TransportadorRepository transportadorRepository;

    private final TransportadorMapper transportadorMapper;

    public TransportadorServiceImpl(TransportadorRepository transportadorRepository, TransportadorMapper transportadorMapper) {
        this.transportadorRepository = transportadorRepository;
        this.transportadorMapper = transportadorMapper;
    }

    @Override
    public TransportadorDTO save(TransportadorDTO transportadorDTO) {
        log.debug("Request to save Transportador : {}", transportadorDTO);
        Transportador transportador = transportadorMapper.toEntity(transportadorDTO);
        transportador = transportadorRepository.save(transportador);
        return transportadorMapper.toDto(transportador);
    }

    @Override
    public Optional<TransportadorDTO> partialUpdate(TransportadorDTO transportadorDTO) {
        log.debug("Request to partially update Transportador : {}", transportadorDTO);

        return transportadorRepository
            .findById(transportadorDTO.getId())
            .map(existingTransportador -> {
                transportadorMapper.partialUpdate(existingTransportador, transportadorDTO);

                return existingTransportador;
            })
            .map(transportadorRepository::save)
            .map(transportadorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransportadorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Transportadors");
        return transportadorRepository.findAll(pageable).map(transportadorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransportadorDTO> findOne(Long id) {
        log.debug("Request to get Transportador : {}", id);
        return transportadorRepository.findById(id).map(transportadorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Transportador : {}", id);
        transportadorRepository.deleteById(id);
    }
}
