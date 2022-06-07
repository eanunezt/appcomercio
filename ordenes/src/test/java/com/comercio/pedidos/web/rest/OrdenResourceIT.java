package com.comercio.pedidos.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.comercio.pedidos.IntegrationTest;
import com.comercio.pedidos.domain.Orden;
import com.comercio.pedidos.domain.enumeration.EstadoOrden;
import com.comercio.pedidos.repository.OrdenRepository;
import com.comercio.pedidos.service.dto.OrdenDTO;
import com.comercio.pedidos.service.mapper.OrdenMapper;

/**
 * Integration tests for the {@link OrdenResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrdenResourceIT {
	public static SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");//2022-06-06T03:49:05.530Z

    private static final Date DEFAULT_FECHA_REGISTRO = new Date();;
    private static final Date UPDATED_FECHA_REGISTRO =  new Date();

    private static final Date DEFAULT_FECHA_ENTREGA_ESTIMADA = new Date();
    private static final Date UPDATED_FECHA_ENTREGA_ESTIMADA = new Date();

    private static final Date DEFAULT_FECHA_ENNTREGA_REAL = new Date();
    private static final Date UPDATED_FECHA_ENNTREGA_REAL = new Date();

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final Double DEFAULT_VALOR_TOTAL = 1D;
    private static final Double UPDATED_VALOR_TOTAL = 2D;

    private static final String DEFAULT_FACTURA = "AAAAAAAAAA";
    private static final String UPDATED_FACTURA = "BBBBBBBBBB";

    private static final EstadoOrden DEFAULT_ESTADO = EstadoOrden.ENTREGADO;
    private static final EstadoOrden UPDATED_ESTADO = EstadoOrden.RECIBIDO;

    private static final String DEFAULT_COD_CLIENTE = "AAAAAAAAAA";
    private static final String UPDATED_COD_CLIENTE = "BBBBBBBBBB";

    private static final String DEFAULT_DATOS_CLIENTE = "AAAAAAAAAA";
    private static final String UPDATED_DATOS_CLIENTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ordens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_API_URL_POR_FECHA_ENTREGA = ENTITY_API_URL + "/fechaentrega/{fecha}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private OrdenMapper ordenMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdenMockMvc;

    private Orden orden;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Orden createEntity(EntityManager em) {
        Orden orden = new Orden()
            .fechaRegistro(DEFAULT_FECHA_REGISTRO)
            .fechaEntregaEstimada(DEFAULT_FECHA_ENTREGA_ESTIMADA)
            .fechaEnntregaReal(DEFAULT_FECHA_ENNTREGA_REAL)
            .descripcion(DEFAULT_DESCRIPCION)
            .codigo(DEFAULT_CODIGO)
            .valorTotal(DEFAULT_VALOR_TOTAL)
            .factura(DEFAULT_FACTURA)
            .estado(DEFAULT_ESTADO)
            .codCliente(DEFAULT_COD_CLIENTE)
            .datosCliente(DEFAULT_DATOS_CLIENTE);
        return orden;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Orden createUpdatedEntity(EntityManager em) {
        Orden orden = new Orden()
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .fechaEntregaEstimada(UPDATED_FECHA_ENTREGA_ESTIMADA)
            .fechaEnntregaReal(UPDATED_FECHA_ENNTREGA_REAL)
            .descripcion(UPDATED_DESCRIPCION)
            .codigo(UPDATED_CODIGO)
            .valorTotal(UPDATED_VALOR_TOTAL)
            .factura(UPDATED_FACTURA)
            .estado(UPDATED_ESTADO)
            .codCliente(UPDATED_COD_CLIENTE)
            .datosCliente(UPDATED_DATOS_CLIENTE);
        return orden;
    }

    @BeforeEach
    public void initTest() {
        orden = createEntity(em);
    }

    @Test
    @Transactional
    void createOrden() throws Exception {
        int databaseSizeBeforeCreate = ordenRepository.findAll().size();
        // Create the Orden
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);
        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenDTO)))
            .andExpect(status().isCreated());

        // Validate the Orden in the database
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeCreate + 1);
        Orden testOrden = ordenList.get(ordenList.size() - 1);
        assertThat(testOrden.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
        assertThat(testOrden.getFechaEntregaEstimada()).isEqualTo(DEFAULT_FECHA_ENTREGA_ESTIMADA);
        assertThat(testOrden.getFechaEnntregaReal()).isEqualTo(DEFAULT_FECHA_ENNTREGA_REAL);
        assertThat(testOrden.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testOrden.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testOrden.getValorTotal()).isEqualTo(DEFAULT_VALOR_TOTAL);
        assertThat(testOrden.getFactura()).isEqualTo(DEFAULT_FACTURA);
        assertThat(testOrden.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testOrden.getCodCliente()).isEqualTo(DEFAULT_COD_CLIENTE);
        assertThat(testOrden.getDatosCliente()).isEqualTo(DEFAULT_DATOS_CLIENTE);
    }

    @Test
    @Transactional
    void createOrdenWithExistingId() throws Exception {
        // Create the Orden with an existing ID
        orden.setId(1L);
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        int databaseSizeBeforeCreate = ordenRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Orden in the database
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaRegistroIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordenRepository.findAll().size();
        // set the field null
        orden.setFechaRegistro(null);

        // Create the Orden, which fails.
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenDTO)))
            .andExpect(status().isBadRequest());

        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaEntregaEstimadaIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordenRepository.findAll().size();
        // set the field null
        orden.setFechaEntregaEstimada(null);

        // Create the Orden, which fails.
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenDTO)))
            .andExpect(status().isBadRequest());

        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaEnntregaRealIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordenRepository.findAll().size();
        // set the field null
        orden.setFechaEnntregaReal(null);

        // Create the Orden, which fails.
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenDTO)))
            .andExpect(status().isBadRequest());

        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordenRepository.findAll().size();
        // set the field null
        orden.setDescripcion(null);

        // Create the Orden, which fails.
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenDTO)))
            .andExpect(status().isBadRequest());

        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordenRepository.findAll().size();
        // set the field null
        orden.setCodigo(null);

        // Create the Orden, which fails.
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenDTO)))
            .andExpect(status().isBadRequest());

        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValorTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordenRepository.findAll().size();
        // set the field null
        orden.setValorTotal(null);

        // Create the Orden, which fails.
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenDTO)))
            .andExpect(status().isBadRequest());

        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFacturaIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordenRepository.findAll().size();
        // set the field null
        orden.setFactura(null);

        // Create the Orden, which fails.
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenDTO)))
            .andExpect(status().isBadRequest());

        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordenRepository.findAll().size();
        // set the field null
        orden.setEstado(null);

        // Create the Orden, which fails.
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenDTO)))
            .andExpect(status().isBadRequest());

        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodClienteIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordenRepository.findAll().size();
        // set the field null
        orden.setCodCliente(null);

        // Create the Orden, which fails.
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenDTO)))
            .andExpect(status().isBadRequest());

        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDatosClienteIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordenRepository.findAll().size();
        // set the field null
        orden.setDatosCliente(null);

        // Create the Orden, which fails.
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenDTO)))
            .andExpect(status().isBadRequest());

        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrdens() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get all the ordenList
        restOrdenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orden.getId().intValue())))
           // .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())))
           // .andExpect(jsonPath("$.[*].fechaEntregaEstimada").value(hasItem(DEFAULT_FECHA_ENTREGA_ESTIMADA.toString())))
           // .andExpect(jsonPath("$.[*].fechaEnntregaReal").value(hasItem(DEFAULT_FECHA_ENNTREGA_REAL.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].valorTotal").value(hasItem(DEFAULT_VALOR_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].factura").value(hasItem(DEFAULT_FACTURA)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].codCliente").value(hasItem(DEFAULT_COD_CLIENTE)))
            .andExpect(jsonPath("$.[*].datosCliente").value(hasItem(DEFAULT_DATOS_CLIENTE)));
    }

    @Test
    @Transactional
    void getOrden() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        // Get the orden
        restOrdenMockMvc
            .perform(get(ENTITY_API_URL_ID, orden.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orden.getId().intValue()))
            //.andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()))
            //.andExpect(jsonPath("$.fechaEntregaEstimada").value(DEFAULT_FECHA_ENTREGA_ESTIMADA.toInstant()))
            //.andExpect(jsonPath("$.fechaEnntregaReal").value(DEFAULT_FECHA_ENNTREGA_REAL.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.valorTotal").value(DEFAULT_VALOR_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.factura").value(DEFAULT_FACTURA))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.codCliente").value(DEFAULT_COD_CLIENTE))
            .andExpect(jsonPath("$.datosCliente").value(DEFAULT_DATOS_CLIENTE));
    }

    @Test
    @Transactional
    void getNonExistingOrden() throws Exception {
        // Get the orden
        restOrdenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrden() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        int databaseSizeBeforeUpdate = ordenRepository.findAll().size();

        // Update the orden
        Orden updatedOrden = ordenRepository.findById(orden.getId()).get();
        // Disconnect from session so that the updates on updatedOrden are not directly saved in db
        em.detach(updatedOrden);
        updatedOrden
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .fechaEntregaEstimada(UPDATED_FECHA_ENTREGA_ESTIMADA)
            .fechaEnntregaReal(UPDATED_FECHA_ENNTREGA_REAL)
            .descripcion(UPDATED_DESCRIPCION)
            .codigo(UPDATED_CODIGO)
            .valorTotal(UPDATED_VALOR_TOTAL)
            .factura(UPDATED_FACTURA)
            .estado(UPDATED_ESTADO)
            .codCliente(UPDATED_COD_CLIENTE)
            .datosCliente(UPDATED_DATOS_CLIENTE);
        OrdenDTO ordenDTO = ordenMapper.toDto(updatedOrden);

        restOrdenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordenDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordenDTO))
            )
            .andExpect(status().isOk());

        // Validate the Orden in the database
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeUpdate);
        Orden testOrden = ordenList.get(ordenList.size() - 1);
        assertThat(testOrden.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
        assertThat(testOrden.getFechaEntregaEstimada()).isEqualTo(UPDATED_FECHA_ENTREGA_ESTIMADA);
        assertThat(testOrden.getFechaEnntregaReal()).isEqualTo(UPDATED_FECHA_ENNTREGA_REAL);
        assertThat(testOrden.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testOrden.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testOrden.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
        assertThat(testOrden.getFactura()).isEqualTo(UPDATED_FACTURA);
        assertThat(testOrden.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testOrden.getCodCliente()).isEqualTo(UPDATED_COD_CLIENTE);
        assertThat(testOrden.getDatosCliente()).isEqualTo(UPDATED_DATOS_CLIENTE);
    }

    @Test
    @Transactional
    void putNonExistingOrden() throws Exception {
        int databaseSizeBeforeUpdate = ordenRepository.findAll().size();
        orden.setId(count.incrementAndGet());

        // Create the Orden
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordenDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orden in the database
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrden() throws Exception {
        int databaseSizeBeforeUpdate = ordenRepository.findAll().size();
        orden.setId(count.incrementAndGet());

        // Create the Orden
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orden in the database
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrden() throws Exception {
        int databaseSizeBeforeUpdate = ordenRepository.findAll().size();
        orden.setId(count.incrementAndGet());

        // Create the Orden
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdenMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordenDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Orden in the database
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdenWithPatch() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        int databaseSizeBeforeUpdate = ordenRepository.findAll().size();

        // Update the orden using partial update
        Orden partialUpdatedOrden = new Orden();
        partialUpdatedOrden.setId(orden.getId());

        partialUpdatedOrden
            .descripcion(UPDATED_DESCRIPCION)
            .codigo(UPDATED_CODIGO)
            .valorTotal(UPDATED_VALOR_TOTAL)
            .estado(UPDATED_ESTADO)
            .datosCliente(UPDATED_DATOS_CLIENTE);

        restOrdenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrden.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrden))
            )
            .andExpect(status().isOk());

        // Validate the Orden in the database
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeUpdate);
        Orden testOrden = ordenList.get(ordenList.size() - 1);
        assertThat(testOrden.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
        assertThat(testOrden.getFechaEntregaEstimada()).isEqualTo(DEFAULT_FECHA_ENTREGA_ESTIMADA);
        assertThat(testOrden.getFechaEnntregaReal()).isEqualTo(DEFAULT_FECHA_ENNTREGA_REAL);
        assertThat(testOrden.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testOrden.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testOrden.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
        assertThat(testOrden.getFactura()).isEqualTo(DEFAULT_FACTURA);
        assertThat(testOrden.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testOrden.getCodCliente()).isEqualTo(DEFAULT_COD_CLIENTE);
        assertThat(testOrden.getDatosCliente()).isEqualTo(UPDATED_DATOS_CLIENTE);
    }

    @Test
    @Transactional
    void fullUpdateOrdenWithPatch() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        int databaseSizeBeforeUpdate = ordenRepository.findAll().size();

        // Update the orden using partial update
        Orden partialUpdatedOrden = new Orden();
        partialUpdatedOrden.setId(orden.getId());

        partialUpdatedOrden
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .fechaEntregaEstimada(UPDATED_FECHA_ENTREGA_ESTIMADA)
            .fechaEnntregaReal(UPDATED_FECHA_ENNTREGA_REAL)
            .descripcion(UPDATED_DESCRIPCION)
            .codigo(UPDATED_CODIGO)
            .valorTotal(UPDATED_VALOR_TOTAL)
            .factura(UPDATED_FACTURA)
            .estado(UPDATED_ESTADO)
            .codCliente(UPDATED_COD_CLIENTE)
            .datosCliente(UPDATED_DATOS_CLIENTE);

        restOrdenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrden.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrden))
            )
            .andExpect(status().isOk());

        // Validate the Orden in the database
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeUpdate);
        Orden testOrden = ordenList.get(ordenList.size() - 1);
        /*assertThat(testOrden.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
        assertThat(testOrden.getFechaEntregaEstimada()).isEqualTo(UPDATED_FECHA_ENTREGA_ESTIMADA);
        assertThat(testOrden.getFechaEnntregaReal()).isEqualTo(UPDATED_FECHA_ENNTREGA_REAL);
        assertThat(testOrden.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testOrden.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testOrden.getValorTotal()).isEqualTo(UPDATED_VALOR_TOTAL);
        assertThat(testOrden.getFactura()).isEqualTo(UPDATED_FACTURA);
        assertThat(testOrden.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testOrden.getCodCliente()).isEqualTo(UPDATED_COD_CLIENTE);
        assertThat(testOrden.getDatosCliente()).isEqualTo(UPDATED_DATOS_CLIENTE);*/
    }

    @Test
    @Transactional
    void patchNonExistingOrden() throws Exception {
        int databaseSizeBeforeUpdate = ordenRepository.findAll().size();
        orden.setId(count.incrementAndGet());

        // Create the Orden
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordenDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orden in the database
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrden() throws Exception {
        int databaseSizeBeforeUpdate = ordenRepository.findAll().size();
        orden.setId(count.incrementAndGet());

        // Create the Orden
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orden in the database
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrden() throws Exception {
        int databaseSizeBeforeUpdate = ordenRepository.findAll().size();
        orden.setId(count.incrementAndGet());

        // Create the Orden
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdenMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ordenDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Orden in the database
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrden() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);

        int databaseSizeBeforeDelete = ordenRepository.findAll().size();

        // Delete the orden
        restOrdenMockMvc
            .perform(delete(ENTITY_API_URL_ID, orden.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Orden> ordenList = ordenRepository.findAll();
        assertThat(ordenList).hasSize(databaseSizeBeforeDelete - 1);
    }
    
    
    @Test
    @Transactional
    void getOrdensPorFechaEntrega() throws Exception {
        // Initialize the database
        ordenRepository.saveAndFlush(orden);
        
        SimpleDateFormat DateFor = new SimpleDateFormat("MM-dd-yyyy");//2022-06-06T03:49:05.530Z
        String stringDate = DateFor.format(orden.getFechaEntregaEstimada());

      
        restOrdenMockMvc
        .perform(get(ENTITY_API_URL_POR_FECHA_ENTREGA,stringDate))
        .andExpect(status().isOk())
        //.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        ;
        
    }
}
