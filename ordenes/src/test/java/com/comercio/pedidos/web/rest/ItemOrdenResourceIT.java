package com.comercio.pedidos.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.comercio.pedidos.IntegrationTest;
import com.comercio.pedidos.domain.ItemOrden;
import com.comercio.pedidos.repository.ItemOrdenRepository;
import com.comercio.pedidos.service.dto.ItemOrdenDTO;
import com.comercio.pedidos.service.mapper.ItemOrdenMapper;
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
 * Integration tests for the {@link ItemOrdenResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ItemOrdenResourceIT {

    private static final Integer DEFAULT_ITEM = 1;
    private static final Integer UPDATED_ITEM = 2;

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;

    private static final Double DEFAULT_VALOR_UNIDAD = 1D;
    private static final Double UPDATED_VALOR_UNIDAD = 2D;

    private static final Double DEFAULT_VALOR = 1D;
    private static final Double UPDATED_VALOR = 2D;

    private static final String DEFAULT_COD_PRODUCTO = "AAAAAAAAAA";
    private static final String UPDATED_COD_PRODUCTO = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_PRODCUTO = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PRODCUTO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/item-ordens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ItemOrdenRepository itemOrdenRepository;

    @Autowired
    private ItemOrdenMapper itemOrdenMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemOrdenMockMvc;

    private ItemOrden itemOrden;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemOrden createEntity(EntityManager em) {
        ItemOrden itemOrden = new ItemOrden()
            .item(DEFAULT_ITEM)
            .cantidad(DEFAULT_CANTIDAD)
            .valorUnidad(DEFAULT_VALOR_UNIDAD)
            .valor(DEFAULT_VALOR)
            .codProducto(DEFAULT_COD_PRODUCTO)
            .nomProdcuto(DEFAULT_NOM_PRODCUTO);
        return itemOrden;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemOrden createUpdatedEntity(EntityManager em) {
        ItemOrden itemOrden = new ItemOrden()
            .item(UPDATED_ITEM)
            .cantidad(UPDATED_CANTIDAD)
            .valorUnidad(UPDATED_VALOR_UNIDAD)
            .valor(UPDATED_VALOR)
            .codProducto(UPDATED_COD_PRODUCTO)
            .nomProdcuto(UPDATED_NOM_PRODCUTO);
        return itemOrden;
    }

    @BeforeEach
    public void initTest() {
        itemOrden = createEntity(em);
    }

    @Test
    @Transactional
    void createItemOrden() throws Exception {
        int databaseSizeBeforeCreate = itemOrdenRepository.findAll().size();
        // Create the ItemOrden
        ItemOrdenDTO itemOrdenDTO = itemOrdenMapper.toDto(itemOrden);
        restItemOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemOrdenDTO)))
            .andExpect(status().isCreated());

        // Validate the ItemOrden in the database
        List<ItemOrden> itemOrdenList = itemOrdenRepository.findAll();
        assertThat(itemOrdenList).hasSize(databaseSizeBeforeCreate + 1);
        ItemOrden testItemOrden = itemOrdenList.get(itemOrdenList.size() - 1);
        assertThat(testItemOrden.getItem()).isEqualTo(DEFAULT_ITEM);
        assertThat(testItemOrden.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testItemOrden.getValorUnidad()).isEqualTo(DEFAULT_VALOR_UNIDAD);
        assertThat(testItemOrden.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testItemOrden.getCodProducto()).isEqualTo(DEFAULT_COD_PRODUCTO);
        assertThat(testItemOrden.getNomProdcuto()).isEqualTo(DEFAULT_NOM_PRODCUTO);
    }

    @Test
    @Transactional
    void createItemOrdenWithExistingId() throws Exception {
        // Create the ItemOrden with an existing ID
        itemOrden.setId(1L);
        ItemOrdenDTO itemOrdenDTO = itemOrdenMapper.toDto(itemOrden);

        int databaseSizeBeforeCreate = itemOrdenRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemOrdenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ItemOrden in the database
        List<ItemOrden> itemOrdenList = itemOrdenRepository.findAll();
        assertThat(itemOrdenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkItemIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemOrdenRepository.findAll().size();
        // set the field null
        itemOrden.setItem(null);

        // Create the ItemOrden, which fails.
        ItemOrdenDTO itemOrdenDTO = itemOrdenMapper.toDto(itemOrden);

        restItemOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemOrdenDTO)))
            .andExpect(status().isBadRequest());

        List<ItemOrden> itemOrdenList = itemOrdenRepository.findAll();
        assertThat(itemOrdenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCantidadIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemOrdenRepository.findAll().size();
        // set the field null
        itemOrden.setCantidad(null);

        // Create the ItemOrden, which fails.
        ItemOrdenDTO itemOrdenDTO = itemOrdenMapper.toDto(itemOrden);

        restItemOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemOrdenDTO)))
            .andExpect(status().isBadRequest());

        List<ItemOrden> itemOrdenList = itemOrdenRepository.findAll();
        assertThat(itemOrdenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValorUnidadIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemOrdenRepository.findAll().size();
        // set the field null
        itemOrden.setValorUnidad(null);

        // Create the ItemOrden, which fails.
        ItemOrdenDTO itemOrdenDTO = itemOrdenMapper.toDto(itemOrden);

        restItemOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemOrdenDTO)))
            .andExpect(status().isBadRequest());

        List<ItemOrden> itemOrdenList = itemOrdenRepository.findAll();
        assertThat(itemOrdenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValorIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemOrdenRepository.findAll().size();
        // set the field null
        itemOrden.setValor(null);

        // Create the ItemOrden, which fails.
        ItemOrdenDTO itemOrdenDTO = itemOrdenMapper.toDto(itemOrden);

        restItemOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemOrdenDTO)))
            .andExpect(status().isBadRequest());

        List<ItemOrden> itemOrdenList = itemOrdenRepository.findAll();
        assertThat(itemOrdenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodProductoIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemOrdenRepository.findAll().size();
        // set the field null
        itemOrden.setCodProducto(null);

        // Create the ItemOrden, which fails.
        ItemOrdenDTO itemOrdenDTO = itemOrdenMapper.toDto(itemOrden);

        restItemOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemOrdenDTO)))
            .andExpect(status().isBadRequest());

        List<ItemOrden> itemOrdenList = itemOrdenRepository.findAll();
        assertThat(itemOrdenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomProdcutoIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemOrdenRepository.findAll().size();
        // set the field null
        itemOrden.setNomProdcuto(null);

        // Create the ItemOrden, which fails.
        ItemOrdenDTO itemOrdenDTO = itemOrdenMapper.toDto(itemOrden);

        restItemOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemOrdenDTO)))
            .andExpect(status().isBadRequest());

        List<ItemOrden> itemOrdenList = itemOrdenRepository.findAll();
        assertThat(itemOrdenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllItemOrdens() throws Exception {
        // Initialize the database
        itemOrdenRepository.saveAndFlush(itemOrden);

        // Get all the itemOrdenList
        restItemOrdenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemOrden.getId().intValue())))
            .andExpect(jsonPath("$.[*].item").value(hasItem(DEFAULT_ITEM)))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].valorUnidad").value(hasItem(DEFAULT_VALOR_UNIDAD.doubleValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())))
            .andExpect(jsonPath("$.[*].codProducto").value(hasItem(DEFAULT_COD_PRODUCTO)))
            .andExpect(jsonPath("$.[*].nomProdcuto").value(hasItem(DEFAULT_NOM_PRODCUTO)));
    }

    @Test
    @Transactional
    void getItemOrden() throws Exception {
        // Initialize the database
        itemOrdenRepository.saveAndFlush(itemOrden);

        // Get the itemOrden
        restItemOrdenMockMvc
            .perform(get(ENTITY_API_URL_ID, itemOrden.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemOrden.getId().intValue()))
            .andExpect(jsonPath("$.item").value(DEFAULT_ITEM))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.valorUnidad").value(DEFAULT_VALOR_UNIDAD.doubleValue()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.doubleValue()))
            .andExpect(jsonPath("$.codProducto").value(DEFAULT_COD_PRODUCTO))
            .andExpect(jsonPath("$.nomProdcuto").value(DEFAULT_NOM_PRODCUTO));
    }

    @Test
    @Transactional
    void getNonExistingItemOrden() throws Exception {
        // Get the itemOrden
        restItemOrdenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewItemOrden() throws Exception {
        // Initialize the database
        itemOrdenRepository.saveAndFlush(itemOrden);

        int databaseSizeBeforeUpdate = itemOrdenRepository.findAll().size();

        // Update the itemOrden
        ItemOrden updatedItemOrden = itemOrdenRepository.findById(itemOrden.getId()).get();
        // Disconnect from session so that the updates on updatedItemOrden are not directly saved in db
        em.detach(updatedItemOrden);
        updatedItemOrden
            .item(UPDATED_ITEM)
            .cantidad(UPDATED_CANTIDAD)
            .valorUnidad(UPDATED_VALOR_UNIDAD)
            .valor(UPDATED_VALOR)
            .codProducto(UPDATED_COD_PRODUCTO)
            .nomProdcuto(UPDATED_NOM_PRODCUTO);
        ItemOrdenDTO itemOrdenDTO = itemOrdenMapper.toDto(updatedItemOrden);

        restItemOrdenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemOrdenDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemOrdenDTO))
            )
            .andExpect(status().isOk());

        // Validate the ItemOrden in the database
        List<ItemOrden> itemOrdenList = itemOrdenRepository.findAll();
        assertThat(itemOrdenList).hasSize(databaseSizeBeforeUpdate);
        ItemOrden testItemOrden = itemOrdenList.get(itemOrdenList.size() - 1);
        assertThat(testItemOrden.getItem()).isEqualTo(UPDATED_ITEM);
        assertThat(testItemOrden.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testItemOrden.getValorUnidad()).isEqualTo(UPDATED_VALOR_UNIDAD);
        assertThat(testItemOrden.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testItemOrden.getCodProducto()).isEqualTo(UPDATED_COD_PRODUCTO);
        assertThat(testItemOrden.getNomProdcuto()).isEqualTo(UPDATED_NOM_PRODCUTO);
    }

    @Test
    @Transactional
    void putNonExistingItemOrden() throws Exception {
        int databaseSizeBeforeUpdate = itemOrdenRepository.findAll().size();
        itemOrden.setId(count.incrementAndGet());

        // Create the ItemOrden
        ItemOrdenDTO itemOrdenDTO = itemOrdenMapper.toDto(itemOrden);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemOrdenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemOrdenDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemOrdenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemOrden in the database
        List<ItemOrden> itemOrdenList = itemOrdenRepository.findAll();
        assertThat(itemOrdenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItemOrden() throws Exception {
        int databaseSizeBeforeUpdate = itemOrdenRepository.findAll().size();
        itemOrden.setId(count.incrementAndGet());

        // Create the ItemOrden
        ItemOrdenDTO itemOrdenDTO = itemOrdenMapper.toDto(itemOrden);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemOrdenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemOrdenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemOrden in the database
        List<ItemOrden> itemOrdenList = itemOrdenRepository.findAll();
        assertThat(itemOrdenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItemOrden() throws Exception {
        int databaseSizeBeforeUpdate = itemOrdenRepository.findAll().size();
        itemOrden.setId(count.incrementAndGet());

        // Create the ItemOrden
        ItemOrdenDTO itemOrdenDTO = itemOrdenMapper.toDto(itemOrden);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemOrdenMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemOrdenDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemOrden in the database
        List<ItemOrden> itemOrdenList = itemOrdenRepository.findAll();
        assertThat(itemOrdenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemOrdenWithPatch() throws Exception {
        // Initialize the database
        itemOrdenRepository.saveAndFlush(itemOrden);

        int databaseSizeBeforeUpdate = itemOrdenRepository.findAll().size();

        // Update the itemOrden using partial update
        ItemOrden partialUpdatedItemOrden = new ItemOrden();
        partialUpdatedItemOrden.setId(itemOrden.getId());

        partialUpdatedItemOrden.cantidad(UPDATED_CANTIDAD).codProducto(UPDATED_COD_PRODUCTO);

        restItemOrdenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemOrden.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemOrden))
            )
            .andExpect(status().isOk());

        // Validate the ItemOrden in the database
        List<ItemOrden> itemOrdenList = itemOrdenRepository.findAll();
        assertThat(itemOrdenList).hasSize(databaseSizeBeforeUpdate);
        ItemOrden testItemOrden = itemOrdenList.get(itemOrdenList.size() - 1);
        assertThat(testItemOrden.getItem()).isEqualTo(DEFAULT_ITEM);
        assertThat(testItemOrden.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testItemOrden.getValorUnidad()).isEqualTo(DEFAULT_VALOR_UNIDAD);
        assertThat(testItemOrden.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testItemOrden.getCodProducto()).isEqualTo(UPDATED_COD_PRODUCTO);
        assertThat(testItemOrden.getNomProdcuto()).isEqualTo(DEFAULT_NOM_PRODCUTO);
    }

    @Test
    @Transactional
    void fullUpdateItemOrdenWithPatch() throws Exception {
        // Initialize the database
        itemOrdenRepository.saveAndFlush(itemOrden);

        int databaseSizeBeforeUpdate = itemOrdenRepository.findAll().size();

        // Update the itemOrden using partial update
        ItemOrden partialUpdatedItemOrden = new ItemOrden();
        partialUpdatedItemOrden.setId(itemOrden.getId());

        partialUpdatedItemOrden
            .item(UPDATED_ITEM)
            .cantidad(UPDATED_CANTIDAD)
            .valorUnidad(UPDATED_VALOR_UNIDAD)
            .valor(UPDATED_VALOR)
            .codProducto(UPDATED_COD_PRODUCTO)
            .nomProdcuto(UPDATED_NOM_PRODCUTO);

        restItemOrdenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemOrden.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemOrden))
            )
            .andExpect(status().isOk());

        // Validate the ItemOrden in the database
        List<ItemOrden> itemOrdenList = itemOrdenRepository.findAll();
        assertThat(itemOrdenList).hasSize(databaseSizeBeforeUpdate);
        ItemOrden testItemOrden = itemOrdenList.get(itemOrdenList.size() - 1);
        assertThat(testItemOrden.getItem()).isEqualTo(UPDATED_ITEM);
        assertThat(testItemOrden.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testItemOrden.getValorUnidad()).isEqualTo(UPDATED_VALOR_UNIDAD);
        assertThat(testItemOrden.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testItemOrden.getCodProducto()).isEqualTo(UPDATED_COD_PRODUCTO);
        assertThat(testItemOrden.getNomProdcuto()).isEqualTo(UPDATED_NOM_PRODCUTO);
    }

    @Test
    @Transactional
    void patchNonExistingItemOrden() throws Exception {
        int databaseSizeBeforeUpdate = itemOrdenRepository.findAll().size();
        itemOrden.setId(count.incrementAndGet());

        // Create the ItemOrden
        ItemOrdenDTO itemOrdenDTO = itemOrdenMapper.toDto(itemOrden);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemOrdenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemOrdenDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemOrdenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemOrden in the database
        List<ItemOrden> itemOrdenList = itemOrdenRepository.findAll();
        assertThat(itemOrdenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItemOrden() throws Exception {
        int databaseSizeBeforeUpdate = itemOrdenRepository.findAll().size();
        itemOrden.setId(count.incrementAndGet());

        // Create the ItemOrden
        ItemOrdenDTO itemOrdenDTO = itemOrdenMapper.toDto(itemOrden);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemOrdenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemOrdenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemOrden in the database
        List<ItemOrden> itemOrdenList = itemOrdenRepository.findAll();
        assertThat(itemOrdenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItemOrden() throws Exception {
        int databaseSizeBeforeUpdate = itemOrdenRepository.findAll().size();
        itemOrden.setId(count.incrementAndGet());

        // Create the ItemOrden
        ItemOrdenDTO itemOrdenDTO = itemOrdenMapper.toDto(itemOrden);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemOrdenMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(itemOrdenDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemOrden in the database
        List<ItemOrden> itemOrdenList = itemOrdenRepository.findAll();
        assertThat(itemOrdenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItemOrden() throws Exception {
        // Initialize the database
        itemOrdenRepository.saveAndFlush(itemOrden);

        int databaseSizeBeforeDelete = itemOrdenRepository.findAll().size();

        // Delete the itemOrden
        restItemOrdenMockMvc
            .perform(delete(ENTITY_API_URL_ID, itemOrden.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemOrden> itemOrdenList = itemOrdenRepository.findAll();
        assertThat(itemOrdenList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
