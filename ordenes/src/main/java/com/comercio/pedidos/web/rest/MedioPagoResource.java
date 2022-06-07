package com.comercio.pedidos.web.rest;

import com.comercio.pedidos.repository.MedioPagoRepository;
import com.comercio.pedidos.service.MedioPagoService;
import com.comercio.pedidos.service.dto.MedioPagoDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.comercio.pedidos.domain.MedioPago}.
 */
@RestController
@RequestMapping("/api")
public class MedioPagoResource {

    private final Logger log = LoggerFactory.getLogger(MedioPagoResource.class);

    private static final String ENTITY_NAME = "ordenesMedioPago";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedioPagoService medioPagoService;

    private final MedioPagoRepository medioPagoRepository;

    public MedioPagoResource(MedioPagoService medioPagoService, MedioPagoRepository medioPagoRepository) {
        this.medioPagoService = medioPagoService;
        this.medioPagoRepository = medioPagoRepository;
    }

    /**
     * {@code POST  /medio-pagos} : Create a new medioPago.
     *
     * @param medioPagoDTO the medioPagoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medioPagoDTO, or with status {@code 400 (Bad Request)} if the medioPago has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/medio-pagos")
    public ResponseEntity<MedioPagoDTO> createMedioPago(@Valid @RequestBody MedioPagoDTO medioPagoDTO) throws URISyntaxException {
        log.debug("REST request to save MedioPago : {}", medioPagoDTO);
        if (medioPagoDTO.getId() != null) {
            throw new BadRequestAlertException("A new medioPago cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MedioPagoDTO result = medioPagoService.save(medioPagoDTO);
        return ResponseEntity
            .created(new URI("/api/medio-pagos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /medio-pagos/:id} : Updates an existing medioPago.
     *
     * @param id the id of the medioPagoDTO to save.
     * @param medioPagoDTO the medioPagoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medioPagoDTO,
     * or with status {@code 400 (Bad Request)} if the medioPagoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medioPagoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/medio-pagos/{id}")
    public ResponseEntity<MedioPagoDTO> updateMedioPago(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MedioPagoDTO medioPagoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MedioPago : {}, {}", id, medioPagoDTO);
        if (medioPagoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medioPagoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medioPagoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MedioPagoDTO result = medioPagoService.save(medioPagoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medioPagoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /medio-pagos/:id} : Partial updates given fields of an existing medioPago, field will ignore if it is null
     *
     * @param id the id of the medioPagoDTO to save.
     * @param medioPagoDTO the medioPagoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medioPagoDTO,
     * or with status {@code 400 (Bad Request)} if the medioPagoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the medioPagoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the medioPagoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/medio-pagos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MedioPagoDTO> partialUpdateMedioPago(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MedioPagoDTO medioPagoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MedioPago partially : {}, {}", id, medioPagoDTO);
        if (medioPagoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medioPagoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medioPagoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MedioPagoDTO> result = medioPagoService.partialUpdate(medioPagoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medioPagoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /medio-pagos} : get all the medioPagos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medioPagos in body.
     */
    @GetMapping("/medio-pagos")
    public List<MedioPagoDTO> getAllMedioPagos() {
        log.debug("REST request to get all MedioPagos");
        return medioPagoService.findAll();
    }

    /**
     * {@code GET  /medio-pagos/:id} : get the "id" medioPago.
     *
     * @param id the id of the medioPagoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medioPagoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/medio-pagos/{id}")
    public ResponseEntity<MedioPagoDTO> getMedioPago(@PathVariable Long id) {
        log.debug("REST request to get MedioPago : {}", id);
        Optional<MedioPagoDTO> medioPagoDTO = medioPagoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medioPagoDTO);
    }

    /**
     * {@code DELETE  /medio-pagos/:id} : delete the "id" medioPago.
     *
     * @param id the id of the medioPagoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/medio-pagos/{id}")
    public ResponseEntity<Void> deleteMedioPago(@PathVariable Long id) {
        log.debug("REST request to delete MedioPago : {}", id);
        medioPagoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
