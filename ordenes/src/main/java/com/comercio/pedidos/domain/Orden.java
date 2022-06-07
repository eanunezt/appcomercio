package com.comercio.pedidos.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.comercio.pedidos.domain.enumeration.EstadoOrden;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Orden.
 */
@Entity
@Table(name = "orden")
public class Orden implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fecha_registro", nullable = false)
    private Date fechaRegistro;

    @NotNull
    @Column(name = "fecha_entrega_estimada", nullable = false)
    private Date fechaEntregaEstimada;

    @NotNull
    @Column(name = "fecha_enntrega_real", nullable = false)
    private Date fechaEnntregaReal;

    @NotNull
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @NotNull
    @Column(name = "codigo", nullable = false)
    private String codigo;

    @NotNull
    @Column(name = "valor_total", nullable = false)
    private Double valorTotal;

    @NotNull
    @Column(name = "factura", nullable = false)
    private String factura;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoOrden estado;

    @NotNull
    @Column(name = "cod_cliente", nullable = false)
    private String codCliente;

    @NotNull
    @Column(name = "datos_cliente", nullable = false)
    private String datosCliente;

    @OneToOne
    @JoinColumn(unique = true)
    private MedioPago mediPago;

    @OneToOne
    @JoinColumn(unique = true)
    private Transportador transportador;

    @OneToMany(mappedBy = "orden")
    @JsonIgnoreProperties(value = { "orden" }, allowSetters = true)
    private Set<ItemOrden> items = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Orden id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Orden fechaRegistro(Date fechaRegistro) {
        this.setFechaRegistro(fechaRegistro);
        return this;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaEntregaEstimada() {
        return this.fechaEntregaEstimada;
    }

    public Orden fechaEntregaEstimada(Date fechaEntregaEstimada) {
        this.setFechaEntregaEstimada(fechaEntregaEstimada);
        return this;
    }

    public void setFechaEntregaEstimada(Date fechaEntregaEstimada) {
        this.fechaEntregaEstimada = fechaEntregaEstimada;
    }

    public Date getFechaEnntregaReal() {
        return this.fechaEnntregaReal;
    }

    public Orden fechaEnntregaReal(Date fechaEnntregaReal) {
        this.setFechaEnntregaReal(fechaEnntregaReal);
        return this;
    }

    public void setFechaEnntregaReal(Date fechaEnntregaReal) {
        this.fechaEnntregaReal = fechaEnntregaReal;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Orden descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public Orden codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Double getValorTotal() {
        return this.valorTotal;
    }

    public Orden valorTotal(Double valorTotal) {
        this.setValorTotal(valorTotal);
        return this;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getFactura() {
        return this.factura;
    }

    public Orden factura(String factura) {
        this.setFactura(factura);
        return this;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public EstadoOrden getEstado() {
        return this.estado;
    }

    public Orden estado(EstadoOrden estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    public String getCodCliente() {
        return this.codCliente;
    }

    public Orden codCliente(String codCliente) {
        this.setCodCliente(codCliente);
        return this;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public String getDatosCliente() {
        return this.datosCliente;
    }

    public Orden datosCliente(String datosCliente) {
        this.setDatosCliente(datosCliente);
        return this;
    }

    public void setDatosCliente(String datosCliente) {
        this.datosCliente = datosCliente;
    }

    public MedioPago getMediPago() {
        return this.mediPago;
    }

    public void setMediPago(MedioPago medioPago) {
        this.mediPago = medioPago;
    }

    public Orden mediPago(MedioPago medioPago) {
        this.setMediPago(medioPago);
        return this;
    }

    public Transportador getTransportador() {
        return this.transportador;
    }

    public void setTransportador(Transportador transportador) {
        this.transportador = transportador;
    }

    public Orden transportador(Transportador transportador) {
        this.setTransportador(transportador);
        return this;
    }

    public Set<ItemOrden> getItems() {
        return this.items;
    }

    public void setItems(Set<ItemOrden> itemOrdens) {
        if (this.items != null) {
            this.items.forEach(i -> i.setOrden(null));
        }
        if (itemOrdens != null) {
            itemOrdens.forEach(i -> i.setOrden(this));
        }
        this.items = itemOrdens;
    }

    public Orden items(Set<ItemOrden> itemOrdens) {
        this.setItems(itemOrdens);
        return this;
    }

    public Orden addItems(ItemOrden itemOrden) {
        this.items.add(itemOrden);
        itemOrden.setOrden(this);
        return this;
    }

    public Orden removeItems(ItemOrden itemOrden) {
        this.items.remove(itemOrden);
        itemOrden.setOrden(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Orden)) {
            return false;
        }
        return id != null && id.equals(((Orden) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Orden{" +
            "id=" + getId() +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            ", fechaEntregaEstimada='" + getFechaEntregaEstimada() + "'" +
            ", fechaEnntregaReal='" + getFechaEnntregaReal() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", codigo='" + getCodigo() + "'" +
            ", valorTotal=" + getValorTotal() +
            ", factura='" + getFactura() + "'" +
            ", estado='" + getEstado() + "'" +
            ", codCliente='" + getCodCliente() + "'" +
            ", datosCliente='" + getDatosCliente() + "'" +
            "}";
    }
}
