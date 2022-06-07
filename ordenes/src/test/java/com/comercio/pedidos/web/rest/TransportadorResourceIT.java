package com.comercio.pedidos.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.comercio.pedidos.IntegrationTest;
import com.comercio.pedidos.domain.Transportador;
import com.comercio.pedidos.domain.enumeration.EstadoTransportador;
import com.comercio.pedidos.repository.TransportadorRepository;
import com.comercio.pedidos.service.dto.TransportadorDTO;
import com.comercio.pedidos.service.mapper.TransportadorMapper;
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
 * Integration tests for the {@link TransportadorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TransportadorResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final EstadoTransportador DEFAULT_ESTADO = EstadoTransportador.ACTIVO;
    private static final EstadoTransportador UPDATED_ESTADO = EstadoTransportador.INACTIVO;

    private static final String ENTITY_API_URL = "/api/transportadors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransportadorRepository transportadorRepository;

    @Autowired
    private TransportadorMapper transportadorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransportadorMockMvc;

    private Transportador transportador;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transportador createEntity(EntityManager em) {
        Transportador transportador = new Transportador().nombre(DEFAULT_NOMBRE).codigo(DEFAULT_CODIGO).estado(DEFAULT_ESTADO);
        return transportador;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transportador createUpdatedEntity(EntityManager em) {
        Transportador transportador = new Transportador().nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO).estado(UPDATED_ESTADO);
        return transportador;
    }

    @BeforeEach
    public void initTest() {
        transportador = createEntity(em);
    }

    @Test
    @Transactional
    void createTransportador() throws Exception {
        int databaseSizeBeforeCreate = transportadorRepository.findAll().size();
        // Create the Transportador
        TransportadorDTO transportadorDTO = transportadorMapper.toDto(transportador);
        restTransportadorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transportadorDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Transportador in the database
        List<Transportador> transportadorList = transportadorRepository.findAll();
        assertThat(transportadorList).hasSize(databaseSizeBeforeCreate + 1);
        Transportador testTransportador = transportadorList.get(transportadorList.size() - 1);
        assertThat(testTransportador.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTransportador.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testTransportador.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void createTransportadorWithExistingId() throws Exception {
        // Create the Transportador with an existing ID
        transportador.setId(1L);
        TransportadorDTO transportadorDTO = transportadorMapper.toDto(transportador);

        int databaseSizeBeforeCreate = transportadorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransportadorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transportadorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transportador in the database
        List<Transportador> transportadorList = transportadorRepository.findAll();
        assertThat(transportadorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = transportadorRepository.findAll().size();
        // set the field null
        transportador.setNombre(null);

        // Create the Transportador, which fails.
        TransportadorDTO transportadorDTO = transportadorMapper.toDto(transportador);

        restTransportadorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transportadorDTO))
            )
            .andExpect(status().isBadRequest());

        List<Transportador> transportadorList = transportadorRepository.findAll();
        assertThat(transportadorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = transportadorRepository.findAll().size();
        // set the field null
        transportador.setCodigo(null);

        // Create the Transportador, which fails.
        TransportadorDTO transportadorDTO = transportadorMapper.toDto(transportador);

        restTransportadorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transportadorDTO))
            )
            .andExpect(status().isBadRequest());

        List<Transportador> transportadorList = transportadorRepository.findAll();
        assertThat(transportadorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = transportadorRepository.findAll().size();
        // set the field null
        transportador.setEstado(null);

        // Create the Transportador, which fails.
        TransportadorDTO transportadorDTO = transportadorMapper.toDto(transportador);

        restTransportadorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transportadorDTO))
            )
            .andExpect(status().isBadRequest());

        List<Transportador> transportadorList = transportadorRepository.findAll();
        assertThat(transportadorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransportadors() throws Exception {
        // Initialize the database
        transportadorRepository.saveAndFlush(transportador);

        // Get all the transportadorList
        restTransportadorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transportador.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }

    @Test
    @Transactional
    void getTransportador() throws Exception {
        // Initialize the database
        transportadorRepository.saveAndFlush(transportador);

        // Get the transportador
        restTransportadorMockMvc
            .perform(get(ENTITY_API_URL_ID, transportador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transportador.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTransportador() throws Exception {
        // Get the transportador
        restTransportadorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTransportador() throws Exception {
        // Initialize the database
        transportadorRepository.saveAndFlush(transportador);

        int databaseSizeBeforeUpdate = transportadorRepository.findAll().size();

        // Update the transportador
        Transportador updatedTransportador = transportadorRepository.findById(transportador.getId()).get();
        // Disconnect from session so that the updates on updatedTransportador are not directly saved in db
        em.detach(updatedTransportador);
        updatedTransportador.nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO).estado(UPDATED_ESTADO);
        TransportadorDTO transportadorDTO = transportadorMapper.toDto(updatedTransportador);

        restTransportadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transportadorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transportadorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Transportador in the database
        List<Transportador> transportadorList = transportadorRepository.findAll();
        assertThat(transportadorList).hasSize(databaseSizeBeforeUpdate);
        Transportador testTransportador = transportadorList.get(transportadorList.size() - 1);
        assertThat(testTransportador.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTransportador.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testTransportador.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void putNonExistingTransportador() throws Exception {
        int databaseSizeBeforeUpdate = transportadorRepository.findAll().size();
        transportador.setId(count.incrementAndGet());

        // Create the Transportador
        TransportadorDTO transportadorDTO = transportadorMapper.toDto(transportador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransportadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transportadorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transportadorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transportador in the database
        List<Transportador> transportadorList = transportadorRepository.findAll();
        assertThat(transportadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransportador() throws Exception {
        int databaseSizeBeforeUpdate = transportadorRepository.findAll().size();
        transportador.setId(count.incrementAndGet());

        // Create the Transportador
        TransportadorDTO transportadorDTO = transportadorMapper.toDto(transportador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransportadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transportadorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transportador in the database
        List<Transportador> transportadorList = transportadorRepository.findAll();
        assertThat(transportadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransportador() throws Exception {
        int databaseSizeBeforeUpdate = transportadorRepository.findAll().size();
        transportador.setId(count.incrementAndGet());

        // Create the Transportador
        TransportadorDTO transportadorDTO = transportadorMapper.toDto(transportador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransportadorMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transportadorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transportador in the database
        List<Transportador> transportadorList = transportadorRepository.findAll();
        assertThat(transportadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransportadorWithPatch() throws Exception {
        // Initialize the database
        transportadorRepository.saveAndFlush(transportador);

        int databaseSizeBeforeUpdate = transportadorRepository.findAll().size();

        // Update the transportador using partial update
        Transportador partialUpdatedTransportador = new Transportador();
        partialUpdatedTransportador.setId(transportador.getId());

        partialUpdatedTransportador.nombre(UPDATED_NOMBRE).estado(UPDATED_ESTADO);

        restTransportadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransportador.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransportador))
            )
            .andExpect(status().isOk());

        // Validate the Transportador in the database
        List<Transportador> transportadorList = transportadorRepository.findAll();
        assertThat(transportadorList).hasSize(databaseSizeBeforeUpdate);
        Transportador testTransportador = transportadorList.get(transportadorList.size() - 1);
        assertThat(testTransportador.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTransportador.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testTransportador.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void fullUpdateTransportadorWithPatch() throws Exception {
        // Initialize the database
        transportadorRepository.saveAndFlush(transportador);

        int databaseSizeBeforeUpdate = transportadorRepository.findAll().size();

        // Update the transportador using partial update
        Transportador partialUpdatedTransportador = new Transportador();
        partialUpdatedTransportador.setId(transportador.getId());

        partialUpdatedTransportador.nombre(UPDATED_NOMBRE).codigo(UPDATED_CODIGO).estado(UPDATED_ESTADO);

        restTransportadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransportador.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransportador))
            )
            .andExpect(status().isOk());

        // Validate the Transportador in the database
        List<Transportador> transportadorList = transportadorRepository.findAll();
        assertThat(transportadorList).hasSize(databaseSizeBeforeUpdate);
        Transportador testTransportador = transportadorList.get(transportadorList.size() - 1);
        assertThat(testTransportador.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTransportador.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testTransportador.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void patchNonExistingTransportador() throws Exception {
        int databaseSizeBeforeUpdate = transportadorRepository.findAll().size();
        transportador.setId(count.incrementAndGet());

        // Create the Transportador
        TransportadorDTO transportadorDTO = transportadorMapper.toDto(transportador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransportadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transportadorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transportadorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transportador in the database
        List<Transportador> transportadorList = transportadorRepository.findAll();
        assertThat(transportadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransportador() throws Exception {
        int databaseSizeBeforeUpdate = transportadorRepository.findAll().size();
        transportador.setId(count.incrementAndGet());

        // Create the Transportador
        TransportadorDTO transportadorDTO = transportadorMapper.toDto(transportador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransportadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transportadorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transportador in the database
        List<Transportador> transportadorList = transportadorRepository.findAll();
        assertThat(transportadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransportador() throws Exception {
        int databaseSizeBeforeUpdate = transportadorRepository.findAll().size();
        transportador.setId(count.incrementAndGet());

        // Create the Transportador
        TransportadorDTO transportadorDTO = transportadorMapper.toDto(transportador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransportadorMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transportadorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transportador in the database
        List<Transportador> transportadorList = transportadorRepository.findAll();
        assertThat(transportadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransportador() throws Exception {
        // Initialize the database
        transportadorRepository.saveAndFlush(transportador);

        int databaseSizeBeforeDelete = transportadorRepository.findAll().size();

        // Delete the transportador
        restTransportadorMockMvc
            .perform(delete(ENTITY_API_URL_ID, transportador.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Transportador> transportadorList = transportadorRepository.findAll();
        assertThat(transportadorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
