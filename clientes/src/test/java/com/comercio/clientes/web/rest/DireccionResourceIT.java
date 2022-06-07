package com.comercio.clientes.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.comercio.clientes.IntegrationTest;
import com.comercio.clientes.domain.Direccion;
import com.comercio.clientes.repository.DireccionRepository;
import com.comercio.clientes.service.dto.DireccionDTO;
import com.comercio.clientes.service.mapper.DireccionMapper;
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
 * Integration tests for the {@link DireccionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DireccionResourceIT {

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_CIUDAD = "AAAAAAAAAA";
    private static final String UPDATED_CIUDAD = "BBBBBBBBBB";

    private static final String DEFAULT_BARRIO = "AAAAAAAAAA";
    private static final String UPDATED_BARRIO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/direccions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private DireccionMapper direccionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDireccionMockMvc;

    private Direccion direccion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Direccion createEntity(EntityManager em) {
        Direccion direccion = new Direccion().direccion(DEFAULT_DIRECCION).ciudad(DEFAULT_CIUDAD).barrio(DEFAULT_BARRIO);
        return direccion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Direccion createUpdatedEntity(EntityManager em) {
        Direccion direccion = new Direccion().direccion(UPDATED_DIRECCION).ciudad(UPDATED_CIUDAD).barrio(UPDATED_BARRIO);
        return direccion;
    }

    @BeforeEach
    public void initTest() {
        direccion = createEntity(em);
    }

    @Test
    @Transactional
    void createDireccion() throws Exception {
        int databaseSizeBeforeCreate = direccionRepository.findAll().size();
        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);
        restDireccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(direccionDTO)))
            .andExpect(status().isCreated());

        // Validate the Direccion in the database
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeCreate + 1);
        Direccion testDireccion = direccionList.get(direccionList.size() - 1);
        assertThat(testDireccion.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testDireccion.getCiudad()).isEqualTo(DEFAULT_CIUDAD);
        assertThat(testDireccion.getBarrio()).isEqualTo(DEFAULT_BARRIO);
    }

    @Test
    @Transactional
    void createDireccionWithExistingId() throws Exception {
        // Create the Direccion with an existing ID
        direccion.setId(1L);
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        int databaseSizeBeforeCreate = direccionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDireccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(direccionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDireccionIsRequired() throws Exception {
        int databaseSizeBeforeTest = direccionRepository.findAll().size();
        // set the field null
        direccion.setDireccion(null);

        // Create the Direccion, which fails.
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        restDireccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(direccionDTO)))
            .andExpect(status().isBadRequest());

        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCiudadIsRequired() throws Exception {
        int databaseSizeBeforeTest = direccionRepository.findAll().size();
        // set the field null
        direccion.setCiudad(null);

        // Create the Direccion, which fails.
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        restDireccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(direccionDTO)))
            .andExpect(status().isBadRequest());

        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBarrioIsRequired() throws Exception {
        int databaseSizeBeforeTest = direccionRepository.findAll().size();
        // set the field null
        direccion.setBarrio(null);

        // Create the Direccion, which fails.
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        restDireccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(direccionDTO)))
            .andExpect(status().isBadRequest());

        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDireccions() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList
        restDireccionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(direccion.getId().intValue())))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].ciudad").value(hasItem(DEFAULT_CIUDAD)))
            .andExpect(jsonPath("$.[*].barrio").value(hasItem(DEFAULT_BARRIO)));
    }

    @Test
    @Transactional
    void getDireccion() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get the direccion
        restDireccionMockMvc
            .perform(get(ENTITY_API_URL_ID, direccion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(direccion.getId().intValue()))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.ciudad").value(DEFAULT_CIUDAD))
            .andExpect(jsonPath("$.barrio").value(DEFAULT_BARRIO));
    }

    @Test
    @Transactional
    void getNonExistingDireccion() throws Exception {
        // Get the direccion
        restDireccionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDireccion() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        int databaseSizeBeforeUpdate = direccionRepository.findAll().size();

        // Update the direccion
        Direccion updatedDireccion = direccionRepository.findById(direccion.getId()).get();
        // Disconnect from session so that the updates on updatedDireccion are not directly saved in db
        em.detach(updatedDireccion);
        updatedDireccion.direccion(UPDATED_DIRECCION).ciudad(UPDATED_CIUDAD).barrio(UPDATED_BARRIO);
        DireccionDTO direccionDTO = direccionMapper.toDto(updatedDireccion);

        restDireccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, direccionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(direccionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Direccion in the database
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeUpdate);
        Direccion testDireccion = direccionList.get(direccionList.size() - 1);
        assertThat(testDireccion.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testDireccion.getCiudad()).isEqualTo(UPDATED_CIUDAD);
        assertThat(testDireccion.getBarrio()).isEqualTo(UPDATED_BARRIO);
    }

    @Test
    @Transactional
    void putNonExistingDireccion() throws Exception {
        int databaseSizeBeforeUpdate = direccionRepository.findAll().size();
        direccion.setId(count.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, direccionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(direccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDireccion() throws Exception {
        int databaseSizeBeforeUpdate = direccionRepository.findAll().size();
        direccion.setId(count.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(direccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDireccion() throws Exception {
        int databaseSizeBeforeUpdate = direccionRepository.findAll().size();
        direccion.setId(count.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(direccionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Direccion in the database
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDireccionWithPatch() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        int databaseSizeBeforeUpdate = direccionRepository.findAll().size();

        // Update the direccion using partial update
        Direccion partialUpdatedDireccion = new Direccion();
        partialUpdatedDireccion.setId(direccion.getId());

        partialUpdatedDireccion.direccion(UPDATED_DIRECCION).ciudad(UPDATED_CIUDAD).barrio(UPDATED_BARRIO);

        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDireccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDireccion))
            )
            .andExpect(status().isOk());

        // Validate the Direccion in the database
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeUpdate);
        Direccion testDireccion = direccionList.get(direccionList.size() - 1);
        assertThat(testDireccion.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testDireccion.getCiudad()).isEqualTo(UPDATED_CIUDAD);
        assertThat(testDireccion.getBarrio()).isEqualTo(UPDATED_BARRIO);
    }

    @Test
    @Transactional
    void fullUpdateDireccionWithPatch() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        int databaseSizeBeforeUpdate = direccionRepository.findAll().size();

        // Update the direccion using partial update
        Direccion partialUpdatedDireccion = new Direccion();
        partialUpdatedDireccion.setId(direccion.getId());

        partialUpdatedDireccion.direccion(UPDATED_DIRECCION).ciudad(UPDATED_CIUDAD).barrio(UPDATED_BARRIO);

        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDireccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDireccion))
            )
            .andExpect(status().isOk());

        // Validate the Direccion in the database
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeUpdate);
        Direccion testDireccion = direccionList.get(direccionList.size() - 1);
        assertThat(testDireccion.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testDireccion.getCiudad()).isEqualTo(UPDATED_CIUDAD);
        assertThat(testDireccion.getBarrio()).isEqualTo(UPDATED_BARRIO);
    }

    @Test
    @Transactional
    void patchNonExistingDireccion() throws Exception {
        int databaseSizeBeforeUpdate = direccionRepository.findAll().size();
        direccion.setId(count.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, direccionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(direccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDireccion() throws Exception {
        int databaseSizeBeforeUpdate = direccionRepository.findAll().size();
        direccion.setId(count.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(direccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDireccion() throws Exception {
        int databaseSizeBeforeUpdate = direccionRepository.findAll().size();
        direccion.setId(count.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(direccionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Direccion in the database
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDireccion() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        int databaseSizeBeforeDelete = direccionRepository.findAll().size();

        // Delete the direccion
        restDireccionMockMvc
            .perform(delete(ENTITY_API_URL_ID, direccion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
