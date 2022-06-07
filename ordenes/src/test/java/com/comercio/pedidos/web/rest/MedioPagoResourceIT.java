package com.comercio.pedidos.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.comercio.pedidos.IntegrationTest;
import com.comercio.pedidos.domain.MedioPago;
import com.comercio.pedidos.repository.MedioPagoRepository;
import com.comercio.pedidos.service.dto.MedioPagoDTO;
import com.comercio.pedidos.service.mapper.MedioPagoMapper;
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
 * Integration tests for the {@link MedioPagoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MedioPagoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/medio-pagos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MedioPagoRepository medioPagoRepository;

    @Autowired
    private MedioPagoMapper medioPagoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedioPagoMockMvc;

    private MedioPago medioPago;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedioPago createEntity(EntityManager em) {
        MedioPago medioPago = new MedioPago().nombre(DEFAULT_NOMBRE).codigo(DEFAULT_CODIGO);
        return medioPago;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedioPago createUpdatedEntity(EntityManager em) {
        MedioPago medioPago = new MedioPago().nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO);
        return medioPago;
    }

    @BeforeEach
    public void initTest() {
        medioPago = createEntity(em);
    }

    @Test
    @Transactional
    void createMedioPago() throws Exception {
        int databaseSizeBeforeCreate = medioPagoRepository.findAll().size();
        // Create the MedioPago
        MedioPagoDTO medioPagoDTO = medioPagoMapper.toDto(medioPago);
        restMedioPagoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medioPagoDTO)))
            .andExpect(status().isCreated());

        // Validate the MedioPago in the database
        List<MedioPago> medioPagoList = medioPagoRepository.findAll();
        assertThat(medioPagoList).hasSize(databaseSizeBeforeCreate + 1);
        MedioPago testMedioPago = medioPagoList.get(medioPagoList.size() - 1);
        assertThat(testMedioPago.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testMedioPago.getCodigo()).isEqualTo(DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void createMedioPagoWithExistingId() throws Exception {
        // Create the MedioPago with an existing ID
        medioPago.setId(1L);
        MedioPagoDTO medioPagoDTO = medioPagoMapper.toDto(medioPago);

        int databaseSizeBeforeCreate = medioPagoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedioPagoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medioPagoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MedioPago in the database
        List<MedioPago> medioPagoList = medioPagoRepository.findAll();
        assertThat(medioPagoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = medioPagoRepository.findAll().size();
        // set the field null
        medioPago.setNombre(null);

        // Create the MedioPago, which fails.
        MedioPagoDTO medioPagoDTO = medioPagoMapper.toDto(medioPago);

        restMedioPagoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medioPagoDTO)))
            .andExpect(status().isBadRequest());

        List<MedioPago> medioPagoList = medioPagoRepository.findAll();
        assertThat(medioPagoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = medioPagoRepository.findAll().size();
        // set the field null
        medioPago.setCodigo(null);

        // Create the MedioPago, which fails.
        MedioPagoDTO medioPagoDTO = medioPagoMapper.toDto(medioPago);

        restMedioPagoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medioPagoDTO)))
            .andExpect(status().isBadRequest());

        List<MedioPago> medioPagoList = medioPagoRepository.findAll();
        assertThat(medioPagoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedioPagos() throws Exception {
        // Initialize the database
        medioPagoRepository.saveAndFlush(medioPago);

        // Get all the medioPagoList
        restMedioPagoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medioPago.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)));
    }

    @Test
    @Transactional
    void getMedioPago() throws Exception {
        // Initialize the database
        medioPagoRepository.saveAndFlush(medioPago);

        // Get the medioPago
        restMedioPagoMockMvc
            .perform(get(ENTITY_API_URL_ID, medioPago.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medioPago.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO));
    }

    @Test
    @Transactional
    void getNonExistingMedioPago() throws Exception {
        // Get the medioPago
        restMedioPagoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMedioPago() throws Exception {
        // Initialize the database
        medioPagoRepository.saveAndFlush(medioPago);

        int databaseSizeBeforeUpdate = medioPagoRepository.findAll().size();

        // Update the medioPago
        MedioPago updatedMedioPago = medioPagoRepository.findById(medioPago.getId()).get();
        // Disconnect from session so that the updates on updatedMedioPago are not directly saved in db
        em.detach(updatedMedioPago);
        updatedMedioPago.nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO);
        MedioPagoDTO medioPagoDTO = medioPagoMapper.toDto(updatedMedioPago);

        restMedioPagoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medioPagoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medioPagoDTO))
            )
            .andExpect(status().isOk());

        // Validate the MedioPago in the database
        List<MedioPago> medioPagoList = medioPagoRepository.findAll();
        assertThat(medioPagoList).hasSize(databaseSizeBeforeUpdate);
        MedioPago testMedioPago = medioPagoList.get(medioPagoList.size() - 1);
        assertThat(testMedioPago.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testMedioPago.getCodigo()).isEqualTo(UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void putNonExistingMedioPago() throws Exception {
        int databaseSizeBeforeUpdate = medioPagoRepository.findAll().size();
        medioPago.setId(count.incrementAndGet());

        // Create the MedioPago
        MedioPagoDTO medioPagoDTO = medioPagoMapper.toDto(medioPago);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedioPagoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medioPagoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medioPagoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedioPago in the database
        List<MedioPago> medioPagoList = medioPagoRepository.findAll();
        assertThat(medioPagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedioPago() throws Exception {
        int databaseSizeBeforeUpdate = medioPagoRepository.findAll().size();
        medioPago.setId(count.incrementAndGet());

        // Create the MedioPago
        MedioPagoDTO medioPagoDTO = medioPagoMapper.toDto(medioPago);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedioPagoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medioPagoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedioPago in the database
        List<MedioPago> medioPagoList = medioPagoRepository.findAll();
        assertThat(medioPagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedioPago() throws Exception {
        int databaseSizeBeforeUpdate = medioPagoRepository.findAll().size();
        medioPago.setId(count.incrementAndGet());

        // Create the MedioPago
        MedioPagoDTO medioPagoDTO = medioPagoMapper.toDto(medioPago);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedioPagoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medioPagoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedioPago in the database
        List<MedioPago> medioPagoList = medioPagoRepository.findAll();
        assertThat(medioPagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedioPagoWithPatch() throws Exception {
        // Initialize the database
        medioPagoRepository.saveAndFlush(medioPago);

        int databaseSizeBeforeUpdate = medioPagoRepository.findAll().size();

        // Update the medioPago using partial update
        MedioPago partialUpdatedMedioPago = new MedioPago();
        partialUpdatedMedioPago.setId(medioPago.getId());

        restMedioPagoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedioPago.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMedioPago))
            )
            .andExpect(status().isOk());

        // Validate the MedioPago in the database
        List<MedioPago> medioPagoList = medioPagoRepository.findAll();
        assertThat(medioPagoList).hasSize(databaseSizeBeforeUpdate);
        MedioPago testMedioPago = medioPagoList.get(medioPagoList.size() - 1);
        assertThat(testMedioPago.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testMedioPago.getCodigo()).isEqualTo(DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void fullUpdateMedioPagoWithPatch() throws Exception {
        // Initialize the database
        medioPagoRepository.saveAndFlush(medioPago);

        int databaseSizeBeforeUpdate = medioPagoRepository.findAll().size();

        // Update the medioPago using partial update
        MedioPago partialUpdatedMedioPago = new MedioPago();
        partialUpdatedMedioPago.setId(medioPago.getId());

        partialUpdatedMedioPago.nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO);

        restMedioPagoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedioPago.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMedioPago))
            )
            .andExpect(status().isOk());

        // Validate the MedioPago in the database
        List<MedioPago> medioPagoList = medioPagoRepository.findAll();
        assertThat(medioPagoList).hasSize(databaseSizeBeforeUpdate);
        MedioPago testMedioPago = medioPagoList.get(medioPagoList.size() - 1);
        assertThat(testMedioPago.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testMedioPago.getCodigo()).isEqualTo(UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void patchNonExistingMedioPago() throws Exception {
        int databaseSizeBeforeUpdate = medioPagoRepository.findAll().size();
        medioPago.setId(count.incrementAndGet());

        // Create the MedioPago
        MedioPagoDTO medioPagoDTO = medioPagoMapper.toDto(medioPago);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedioPagoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medioPagoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(medioPagoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedioPago in the database
        List<MedioPago> medioPagoList = medioPagoRepository.findAll();
        assertThat(medioPagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedioPago() throws Exception {
        int databaseSizeBeforeUpdate = medioPagoRepository.findAll().size();
        medioPago.setId(count.incrementAndGet());

        // Create the MedioPago
        MedioPagoDTO medioPagoDTO = medioPagoMapper.toDto(medioPago);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedioPagoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(medioPagoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedioPago in the database
        List<MedioPago> medioPagoList = medioPagoRepository.findAll();
        assertThat(medioPagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedioPago() throws Exception {
        int databaseSizeBeforeUpdate = medioPagoRepository.findAll().size();
        medioPago.setId(count.incrementAndGet());

        // Create the MedioPago
        MedioPagoDTO medioPagoDTO = medioPagoMapper.toDto(medioPago);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedioPagoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(medioPagoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedioPago in the database
        List<MedioPago> medioPagoList = medioPagoRepository.findAll();
        assertThat(medioPagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedioPago() throws Exception {
        // Initialize the database
        medioPagoRepository.saveAndFlush(medioPago);

        int databaseSizeBeforeDelete = medioPagoRepository.findAll().size();

        // Delete the medioPago
        restMedioPagoMockMvc
            .perform(delete(ENTITY_API_URL_ID, medioPago.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MedioPago> medioPagoList = medioPagoRepository.findAll();
        assertThat(medioPagoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
