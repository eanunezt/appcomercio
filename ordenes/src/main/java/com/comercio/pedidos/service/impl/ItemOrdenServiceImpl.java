package com.comercio.pedidos.service.impl;

import com.comercio.pedidos.domain.ItemOrden;
import com.comercio.pedidos.repository.ItemOrdenRepository;
import com.comercio.pedidos.service.ItemOrdenService;
import com.comercio.pedidos.service.dto.ItemOrdenDTO;
import com.comercio.pedidos.service.mapper.ItemOrdenMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ItemOrden}.
 */
@Service
@Transactional
public class ItemOrdenServiceImpl implements ItemOrdenService {

    private final Logger log = LoggerFactory.getLogger(ItemOrdenServiceImpl.class);

    private final ItemOrdenRepository itemOrdenRepository;

    private final ItemOrdenMapper itemOrdenMapper;

    public ItemOrdenServiceImpl(ItemOrdenRepository itemOrdenRepository, ItemOrdenMapper itemOrdenMapper) {
        this.itemOrdenRepository = itemOrdenRepository;
        this.itemOrdenMapper = itemOrdenMapper;
    }

    @Override
    public ItemOrdenDTO save(ItemOrdenDTO itemOrdenDTO) {
        log.debug("Request to save ItemOrden : {}", itemOrdenDTO);
        ItemOrden itemOrden = itemOrdenMapper.toEntity(itemOrdenDTO);
        itemOrden = itemOrdenRepository.save(itemOrden);
        return itemOrdenMapper.toDto(itemOrden);
    }

    @Override
    public Optional<ItemOrdenDTO> partialUpdate(ItemOrdenDTO itemOrdenDTO) {
        log.debug("Request to partially update ItemOrden : {}", itemOrdenDTO);

        return itemOrdenRepository
            .findById(itemOrdenDTO.getId())
            .map(existingItemOrden -> {
                itemOrdenMapper.partialUpdate(existingItemOrden, itemOrdenDTO);

                return existingItemOrden;
            })
            .map(itemOrdenRepository::save)
            .map(itemOrdenMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ItemOrdenDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ItemOrdens");
        return itemOrdenRepository.findAll(pageable).map(itemOrdenMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ItemOrdenDTO> findOne(Long id) {
        log.debug("Request to get ItemOrden : {}", id);
        return itemOrdenRepository.findById(id).map(itemOrdenMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ItemOrden : {}", id);
        itemOrdenRepository.deleteById(id);
    }
}
