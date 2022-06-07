package com.comercio.productos.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.comercio.productos.IntegrationTest;
import com.comercio.productos.domain.TipoProducto;
import com.comercio.productos.repository.TipoProductoRepository;
import com.comercio.productos.service.dto.TipoProductoDTO;
import com.comercio.productos.service.mapper.TipoProductoMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TipoProductoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TipoProductoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tipo-productos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TipoProductoRepository tipoProductoRepository;

    @Autowired
    private TipoProductoMapper tipoProductoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoProductoMockMvc;

    private TipoProducto tipoProducto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoProducto createEntity(EntityManager em) {
        TipoProducto tipoProducto = new TipoProducto().nombre(DEFAULT_NOMBRE).codigo(DEFAULT_CODIGO);
        return tipoProducto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoProducto createUpdatedEntity(EntityManager em) {
        TipoProducto tipoProducto = new TipoProducto().nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO);
        return tipoProducto;
    }

    @BeforeEach
    public void initTest() {
        tipoProducto = createEntity(em);
    }

    @Test
    @Transactional
    void createTipoProducto() throws Exception {
        int databaseSizeBeforeCreate = tipoProductoRepository.findAll().size();
        // Create the TipoProducto
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);
        restTipoProductoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoProductoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TipoProducto in the database
        List<TipoProducto> tipoProductoList = tipoProductoRepository.findAll();
        assertThat(tipoProductoList).hasSize(databaseSizeBeforeCreate + 1);
        TipoProducto testTipoProducto = tipoProductoList.get(tipoProductoList.size() - 1);
        assertThat(testTipoProducto.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTipoProducto.getCodigo()).isEqualTo(DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void createTipoProductoWithExistingId() throws Exception {
        // Create the TipoProducto with an existing ID
        tipoProducto.setId(1L);
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);

        int databaseSizeBeforeCreate = tipoProductoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoProductoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoProducto in the database
        List<TipoProducto> tipoProductoList = tipoProductoRepository.findAll();
        assertThat(tipoProductoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoProductoRepository.findAll().size();
        // set the field null
        tipoProducto.setNombre(null);

        // Create the TipoProducto, which fails.
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);

        restTipoProductoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoProductoDTO))
            )
            .andExpect(status().isBadRequest());

        List<TipoProducto> tipoProductoList = tipoProductoRepository.findAll();
        assertThat(tipoProductoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoProductoRepository.findAll().size();
        // set the field null
        tipoProducto.setCodigo(null);

        // Create the TipoProducto, which fails.
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);

        restTipoProductoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoProductoDTO))
            )
            .andExpect(status().isBadRequest());

        List<TipoProducto> tipoProductoList = tipoProductoRepository.findAll();
        assertThat(tipoProductoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTipoProductos() throws Exception {
        // Initialize the database
        tipoProductoRepository.saveAndFlush(tipoProducto);

        // Get all the tipoProductoList
        restTipoProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoProducto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)));
    }

    @Test
    @Transactional
    void getTipoProducto() throws Exception {
        // Initialize the database
        tipoProductoRepository.saveAndFlush(tipoProducto);

        // Get the tipoProducto
        restTipoProductoMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoProducto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoProducto.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO));
    }

    @Test
    @Transactional
    void getNonExistingTipoProducto() throws Exception {
        // Get the tipoProducto
        restTipoProductoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTipoProducto() throws Exception {
        // Initialize the database
        tipoProductoRepository.saveAndFlush(tipoProducto);

        int databaseSizeBeforeUpdate = tipoProductoRepository.findAll().size();

        // Update the tipoProducto
        TipoProducto updatedTipoProducto = tipoProductoRepository.findById(tipoProducto.getId()).get();
        // Disconnect from session so that the updates on updatedTipoProducto are not directly saved in db
        em.detach(updatedTipoProducto);
        updatedTipoProducto.nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO);
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(updatedTipoProducto);

        restTipoProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoProductoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoProductoDTO))
            )
            .andExpect(status().isOk());

        // Validate the TipoProducto in the database
        List<TipoProducto> tipoProductoList = tipoProductoRepository.findAll();
        assertThat(tipoProductoList).hasSize(databaseSizeBeforeUpdate);
        TipoProducto testTipoProducto = tipoProductoList.get(tipoProductoList.size() - 1);
        assertThat(testTipoProducto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTipoProducto.getCodigo()).isEqualTo(UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void putNonExistingTipoProducto() throws Exception {
        int databaseSizeBeforeUpdate = tipoProductoRepository.findAll().size();
        tipoProducto.setId(count.incrementAndGet());

        // Create the TipoProducto
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoProductoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoProducto in the database
        List<TipoProducto> tipoProductoList = tipoProductoRepository.findAll();
        assertThat(tipoProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoProducto() throws Exception {
        int databaseSizeBeforeUpdate = tipoProductoRepository.findAll().size();
        tipoProducto.setId(count.incrementAndGet());

        // Create the TipoProducto
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoProducto in the database
        List<TipoProducto> tipoProductoList = tipoProductoRepository.findAll();
        assertThat(tipoProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoProducto() throws Exception {
        int databaseSizeBeforeUpdate = tipoProductoRepository.findAll().size();
        tipoProducto.setId(count.incrementAndGet());

        // Create the TipoProducto
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoProductoMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoProductoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoProducto in the database
        List<TipoProducto> tipoProductoList = tipoProductoRepository.findAll();
        assertThat(tipoProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoProductoWithPatch() throws Exception {
        // Initialize the database
        tipoProductoRepository.saveAndFlush(tipoProducto);

        int databaseSizeBeforeUpdate = tipoProductoRepository.findAll().size();

        // Update the tipoProducto using partial update
        TipoProducto partialUpdatedTipoProducto = new TipoProducto();
        partialUpdatedTipoProducto.setId(tipoProducto.getId());

        restTipoProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoProducto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoProducto))
            )
            .andExpect(status().isOk());

        // Validate the TipoProducto in the database
        List<TipoProducto> tipoProductoList = tipoProductoRepository.findAll();
        assertThat(tipoProductoList).hasSize(databaseSizeBeforeUpdate);
        TipoProducto testTipoProducto = tipoProductoList.get(tipoProductoList.size() - 1);
        assertThat(testTipoProducto.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTipoProducto.getCodigo()).isEqualTo(DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void fullUpdateTipoProductoWithPatch() throws Exception {
        // Initialize the database
        tipoProductoRepository.saveAndFlush(tipoProducto);

        int databaseSizeBeforeUpdate = tipoProductoRepository.findAll().size();

        // Update the tipoProducto using partial update
        TipoProducto partialUpdatedTipoProducto = new TipoProducto();
        partialUpdatedTipoProducto.setId(tipoProducto.getId());

        partialUpdatedTipoProducto.nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO);

        restTipoProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoProducto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoProducto))
            )
            .andExpect(status().isOk());

        // Validate the TipoProducto in the database
        List<TipoProducto> tipoProductoList = tipoProductoRepository.findAll();
        assertThat(tipoProductoList).hasSize(databaseSizeBeforeUpdate);
        TipoProducto testTipoProducto = tipoProductoList.get(tipoProductoList.size() - 1);
        assertThat(testTipoProducto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTipoProducto.getCodigo()).isEqualTo(UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void patchNonExistingTipoProducto() throws Exception {
        int databaseSizeBeforeUpdate = tipoProductoRepository.findAll().size();
        tipoProducto.setId(count.incrementAndGet());

        // Create the TipoProducto
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoProductoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoProducto in the database
        List<TipoProducto> tipoProductoList = tipoProductoRepository.findAll();
        assertThat(tipoProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoProducto() throws Exception {
        int databaseSizeBeforeUpdate = tipoProductoRepository.findAll().size();
        tipoProducto.setId(count.incrementAndGet());

        // Create the TipoProducto
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoProductoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoProducto in the database
        List<TipoProducto> tipoProductoList = tipoProductoRepository.findAll();
        assertThat(tipoProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoProducto() throws Exception {
        int databaseSizeBeforeUpdate = tipoProductoRepository.findAll().size();
        tipoProducto.setId(count.incrementAndGet());

        // Create the TipoProducto
        TipoProductoDTO tipoProductoDTO = tipoProductoMapper.toDto(tipoProducto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoProductoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoProductoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoProducto in the database
        List<TipoProducto> tipoProductoList = tipoProductoRepository.findAll();
        assertThat(tipoProductoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoProducto() throws Exception {
        // Initialize the database
        tipoProductoRepository.saveAndFlush(tipoProducto);

        int databaseSizeBeforeDelete = tipoProductoRepository.findAll().size();

        // Delete the tipoProducto
        restTipoProductoMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoProducto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoProducto> tipoProductoList = tipoProductoRepository.findAll();
        assertThat(tipoProductoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
