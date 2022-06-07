package com.comercio.pedidos.web.rest;

import com.comercio.pedidos.repository.ItemOrdenRepository;
import com.comercio.pedidos.service.ItemOrdenService;
import com.comercio.pedidos.service.dto.ItemOrdenDTO;
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
 * REST controller for managing {@link com.comercio.pedidos.domain.ItemOrden}.
 */
@RestController
@RequestMapping("/api")
public class ItemOrdenResource {

    private final Logger log = LoggerFactory.getLogger(ItemOrdenResource.class);

    private static final String ENTITY_NAME = "ordenesItemOrden";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemOrdenService itemOrdenService;

    private final ItemOrdenRepository itemOrdenRepository;

    public ItemOrdenResource(ItemOrdenService itemOrdenService, ItemOrdenRepository itemOrdenRepository) {
        this.itemOrdenService = itemOrdenService;
        this.itemOrdenRepository = itemOrdenRepository;
    }

    /**
     * {@code POST  /item-ordens} : Create a new itemOrden.
     *
     * @param itemOrdenDTO the itemOrdenDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemOrdenDTO, or with status {@code 400 (Bad Request)} if the itemOrden has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/item-ordens")
    public ResponseEntity<ItemOrdenDTO> createItemOrden(@Valid @RequestBody ItemOrdenDTO itemOrdenDTO) throws URISyntaxException {
        log.debug("REST request to save ItemOrden : {}", itemOrdenDTO);
        if (itemOrdenDTO.getId() != null) {
            throw new BadRequestAlertException("A new itemOrden cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemOrdenDTO result = itemOrdenService.save(itemOrdenDTO);
        return ResponseEntity
            .created(new URI("/api/item-ordens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /item-ordens/:id} : Updates an existing itemOrden.
     *
     * @param id the id of the itemOrdenDTO to save.
     * @param itemOrdenDTO the itemOrdenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemOrdenDTO,
     * or with status {@code 400 (Bad Request)} if the itemOrdenDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemOrdenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/item-ordens/{id}")
    public ResponseEntity<ItemOrdenDTO> updateItemOrden(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ItemOrdenDTO itemOrdenDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ItemOrden : {}, {}", id, itemOrdenDTO);
        if (itemOrdenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemOrdenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemOrdenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ItemOrdenDTO result = itemOrdenService.save(itemOrdenDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemOrdenDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /item-ordens/:id} : Partial updates given fields of an existing itemOrden, field will ignore if it is null
     *
     * @param id the id of the itemOrdenDTO to save.
     * @param itemOrdenDTO the itemOrdenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemOrdenDTO,
     * or with status {@code 400 (Bad Request)} if the itemOrdenDTO is not valid,
     * or with status {@code 404 (Not Found)} if the itemOrdenDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemOrdenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/item-ordens/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ItemOrdenDTO> partialUpdateItemOrden(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ItemOrdenDTO itemOrdenDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemOrden partially : {}, {}", id, itemOrdenDTO);
        if (itemOrdenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemOrdenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemOrdenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ItemOrdenDTO> result = itemOrdenService.partialUpdate(itemOrdenDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemOrdenDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /item-ordens} : get all the itemOrdens.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemOrdens in body.
     */
    @GetMapping("/item-ordens")
    public ResponseEntity<List<ItemOrdenDTO>> getAllItemOrdens(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ItemOrdens");
        Page<ItemOrdenDTO> page = itemOrdenService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /item-ordens/:id} : get the "id" itemOrden.
     *
     * @param id the id of the itemOrdenDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemOrdenDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/item-ordens/{id}")
    public ResponseEntity<ItemOrdenDTO> getItemOrden(@PathVariable Long id) {
        log.debug("REST request to get ItemOrden : {}", id);
        Optional<ItemOrdenDTO> itemOrdenDTO = itemOrdenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemOrdenDTO);
    }

    /**
     * {@code DELETE  /item-ordens/:id} : delete the "id" itemOrden.
     *
     * @param id the id of the itemOrdenDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/item-ordens/{id}")
    public ResponseEntity<Void> deleteItemOrden(@PathVariable Long id) {
        log.debug("REST request to delete ItemOrden : {}", id);
        itemOrdenService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
