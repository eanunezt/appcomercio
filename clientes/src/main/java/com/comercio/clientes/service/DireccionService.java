package com.comercio.clientes.service;

import com.comercio.clientes.service.dto.DireccionDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.comercio.clientes.domain.Direccion}.
 */
public interface DireccionService {
    /**
     * Save a direccion.
     *
     * @param direccionDTO the entity to save.
     * @return the persisted entity.
     */
    DireccionDTO save(DireccionDTO direccionDTO);

    /**
     * Partially updates a direccion.
     *
     * @param direccionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DireccionDTO> partialUpdate(DireccionDTO direccionDTO);

    /**
     * Get all the direccions.
     *
     * @return the list of entities.
     */
    List<DireccionDTO> findAll();

    /**
     * Get the "id" direccion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DireccionDTO> findOne(Long id);

    /**
     * Delete the "id" direccion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
