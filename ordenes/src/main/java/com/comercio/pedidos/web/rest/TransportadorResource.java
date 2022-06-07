package com.comercio.pedidos.web.rest;

import com.comercio.pedidos.repository.TransportadorRepository;
import com.comercio.pedidos.service.TransportadorService;
import com.comercio.pedidos.service.dto.TransportadorDTO;
import com.comercio.pedidos.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.comercio.pedidos.domain.Transportador}.
 */
@RestController
@RequestMapping("/api")
public class TransportadorResource {

    private final Logger log = LoggerFactory.getLogger(TransportadorResource.class);

    private static final String ENTITY_NAME = "ordenesTransportador";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransportadorService transportadorService;

    private final TransportadorRepository transportadorRepository;

    public TransportadorResource(TransportadorService transportadorService, TransportadorRepository transportadorRepository) {
        this.transportadorService = transportadorService;
        this.transportadorRepository = transportadorRepository;
    }

    /**
     * {@code POST  /transportadors} : Create a new transportador.
     *
     * @param transportadorDTO the transportadorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transportadorDTO, or with status {@code 400 (Bad Request)} if the transportador has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transportadors")
    public ResponseEntity<TransportadorDTO> createTransportador(@Valid @RequestBody TransportadorDTO transportadorDTO)
        throws URISyntaxException {
        log.debug("REST request to save Transportador : {}", transportadorDTO);
        if (transportadorDTO.getId() != null) {
            throw new BadRequestAlertException("A new transportador cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransportadorDTO result = transportadorService.save(transportadorDTO);
        return ResponseEntity
            .created(new URI("/api/transportadors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transportadors/:id} : Updates an existing transportador.
     *
     * @param id the id of the transportadorDTO to save.
     * @param transportadorDTO the transportadorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transportadorDTO,
     * or with status {@code 400 (Bad Request)} if the transportadorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transportadorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transportadors/{id}")
    public ResponseEntity<TransportadorDTO> updateTransportador(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransportadorDTO transportadorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Transportador : {}, {}", id, transportadorDTO);
        if (transportadorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transportadorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transportadorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TransportadorDTO result = transportadorService.save(transportadorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transportadorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /transportadors/:id} : Partial updates given fields of an existing transportador, field will ignore if it is null
     *
     * @param id the id of the transportadorDTO to save.
     * @param transportadorDTO the transportadorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transportadorDTO,
     * or with status {@code 400 (Bad Request)} if the transportadorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transportadorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transportadorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/transportadors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransportadorDTO> partialUpdateTransportador(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransportadorDTO transportadorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Transportador partially : {}, {}", id, transportadorDTO);
        if (transportadorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transportadorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transportadorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransportadorDTO> result = transportadorService.partialUpdate(transportadorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transportadorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transportadors} : get all the transportadors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transportadors in body.
     */
    @GetMapping("/transportadors")
    public ResponseEntity<List<TransportadorDTO>> getAllTransportadors(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Transportadors");
        Page<TransportadorDTO> page = transportadorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transportadors/:id} : get the "id" transportador.
     *
     * @param id the id of the transportadorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transportadorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transportadors/{id}")
    public ResponseEntity<TransportadorDTO> getTransportador(@PathVariable Long id) {
        log.debug("REST request to get Transportador : {}", id);
        Optional<TransportadorDTO> transportadorDTO = transportadorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transportadorDTO);
    }

    /**
     * {@code DELETE  /transportadors/:id} : delete the "id" transportador.
     *
     * @param id the id of the transportadorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transportadors/{id}")
    public ResponseEntity<Void> deleteTransportador(@PathVariable Long id) {
        log.debug("REST request to delete Transportador : {}", id);
        transportadorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
