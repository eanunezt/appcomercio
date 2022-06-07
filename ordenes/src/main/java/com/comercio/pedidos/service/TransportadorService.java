package com.comercio.pedidos.service;

import com.comercio.pedidos.service.dto.TransportadorDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.comercio.pedidos.domain.Transportador}.
 */
public interface TransportadorService {
    /**
     * Save a transportador.
     *
     * @param transportadorDTO the entity to save.
     * @return the persisted entity.
     */
    TransportadorDTO save(TransportadorDTO transportadorDTO);

    /**
     * Partially updates a transportador.
     *
     * @param transportadorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransportadorDTO> partialUpdate(TransportadorDTO transportadorDTO);

    /**
     * Get all the transportadors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransportadorDTO> findAll(Pageable pageable);

    /**
     * Get the "id" transportador.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransportadorDTO> findOne(Long id);

    /**
     * Delete the "id" transportador.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
