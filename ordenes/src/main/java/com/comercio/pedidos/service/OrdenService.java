package com.comercio.pedidos.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.comercio.pedidos.domain.Orden;
import com.comercio.pedidos.excepciones.ValidacionExcepcion;
import com.comercio.pedidos.service.dto.OrdenDTO;

/**
 * Service Interface for managing {@link com.comercio.pedidos.domain.Orden}.
 */
public interface OrdenService {
    /**
     * Save a orden.
     *
     * @param ordenDTO the entity to save.
     * @return the persisted entity.
     */
    OrdenDTO save(OrdenDTO ordenDTO);

    /**
     * Partially updates a orden.
     *
     * @param ordenDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdenDTO> partialUpdate(OrdenDTO ordenDTO);

    /**
     * Get all the ordens.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrdenDTO> findAll(Pageable pageable);

    /**
     * Get the "id" orden.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdenDTO> findOne(Long id);

    /**
     * Delete the "id" orden.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
    

    /**
     * Obtener orórdenes que deben entregarse en una fecha específica
     *
     * @param fechaEntrega en format dd-MM-yyyy
     * @return the entity.
     * @throws ValidacionExcepcion 
     */
    List<OrdenDTO> findOrdenesPorFechaDeEntrega(String fechaEntrega) throws ValidacionExcepcion;
}
