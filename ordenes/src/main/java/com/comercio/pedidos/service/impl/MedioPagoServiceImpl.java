package com.comercio.pedidos.service.impl;

import com.comercio.pedidos.domain.MedioPago;
import com.comercio.pedidos.repository.MedioPagoRepository;
import com.comercio.pedidos.service.MedioPagoService;
import com.comercio.pedidos.service.dto.MedioPagoDTO;
import com.comercio.pedidos.service.mapper.MedioPagoMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MedioPago}.
 */
@Service
@Transactional
public class MedioPagoServiceImpl implements MedioPagoService {

    private final Logger log = LoggerFactory.getLogger(MedioPagoServiceImpl.class);

    private final MedioPagoRepository medioPagoRepository;

    private final MedioPagoMapper medioPagoMapper;

    public MedioPagoServiceImpl(MedioPagoRepository medioPagoRepository, MedioPagoMapper medioPagoMapper) {
        this.medioPagoRepository = medioPagoRepository;
        this.medioPagoMapper = medioPagoMapper;
    }

    @Override
    public MedioPagoDTO save(MedioPagoDTO medioPagoDTO) {
        log.debug("Request to save MedioPago : {}", medioPagoDTO);
        MedioPago medioPago = medioPagoMapper.toEntity(medioPagoDTO);
        medioPago = medioPagoRepository.save(medioPago);
        return medioPagoMapper.toDto(medioPago);
    }

    @Override
    public Optional<MedioPagoDTO> partialUpdate(MedioPagoDTO medioPagoDTO) {
        log.debug("Request to partially update MedioPago : {}", medioPagoDTO);

        return medioPagoRepository
            .findById(medioPagoDTO.getId())
            .map(existingMedioPago -> {
                medioPagoMapper.partialUpdate(existingMedioPago, medioPagoDTO);

                return existingMedioPago;
            })
            .map(medioPagoRepository::save)
            .map(medioPagoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedioPagoDTO> findAll() {
        log.debug("Request to get all MedioPagos");
        return medioPagoRepository.findAll().stream().map(medioPagoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MedioPagoDTO> findOne(Long id) {
        log.debug("Request to get MedioPago : {}", id);
        return medioPagoRepository.findById(id).map(medioPagoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MedioPago : {}", id);
        medioPagoRepository.deleteById(id);
    }
}
