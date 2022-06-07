package com.comercio.pedidos.service;

import com.comercio.pedidos.service.dto.ItemOrdenDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.comercio.pedidos.domain.ItemOrden}.
 */
public interface ItemOrdenService {
    /**
     * Save a itemOrden.
     *
     * @param itemOrdenDTO the entity to save.
     * @return the persisted entity.
     */
    ItemOrdenDTO save(ItemOrdenDTO itemOrdenDTO);

    /**
     * Partially updates a itemOrden.
     *
     * @param itemOrdenDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ItemOrdenDTO> partialUpdate(ItemOrdenDTO itemOrdenDTO);

    /**
     * Get all the itemOrdens.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ItemOrdenDTO> findAll(Pageable pageable);

    /**
     * Get the "id" itemOrden.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ItemOrdenDTO> findOne(Long id);

    /**
     * Delete the "id" itemOrden.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
