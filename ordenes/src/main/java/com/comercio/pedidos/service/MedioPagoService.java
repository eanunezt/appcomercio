package com.comercio.pedidos.service;

import com.comercio.pedidos.service.dto.MedioPagoDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.comercio.pedidos.domain.MedioPago}.
 */
public interface MedioPagoService {
    /**
     * Save a medioPago.
     *
     * @param medioPagoDTO the entity to save.
     * @return the persisted entity.
     */
    MedioPagoDTO save(MedioPagoDTO medioPagoDTO);

    /**
     * Partially updates a medioPago.
     *
     * @param medioPagoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MedioPagoDTO> partialUpdate(MedioPagoDTO medioPagoDTO);

    /**
     * Get all the medioPagos.
     *
     * @return the list of entities.
     */
    List<MedioPagoDTO> findAll();

    /**
     * Get the "id" medioPago.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MedioPagoDTO> findOne(Long id);

    /**
     * Delete the "id" medioPago.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
