package com.comercio.pedidos.service.dto;

import com.comercio.pedidos.domain.enumeration.EstadoOrden;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.comercio.pedidos.domain.Orden} entity.
 */
public class OrdenDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant fechaRegistro;

    @NotNull
    private Instant fechaEntregaEstimada;

    @NotNull
    private Instant fechaEnntregaReal;

    @NotNull
    private String descripcion;

    @NotNull
    private String codigo;

    @NotNull
    private Double valorTotal;

    @NotNull
    private String factura;

    @NotNull
    private EstadoOrden estado;

    @NotNull
    private String codCliente;

    @NotNull
    private String datosCliente;

    private MedioPagoDTO mediPago;

    private TransportadorDTO transportador;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Instant getFechaEntregaEstimada() {
        return fechaEntregaEstimada;
    }

    public void setFechaEntregaEstimada(Instant fechaEntregaEstimada) {
        this.fechaEntregaEstimada = fechaEntregaEstimada;
    }

    public Instant getFechaEnntregaReal() {
        return fechaEnntregaReal;
    }

    public void setFechaEnntregaReal(Instant fechaEnntregaReal) {
        this.fechaEnntregaReal = fechaEnntregaReal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public String getDatosCliente() {
        return datosCliente;
    }

    public void setDatosCliente(String datosCliente) {
        this.datosCliente = datosCliente;
    }

    public MedioPagoDTO getMediPago() {
        return mediPago;
    }

    public void setMediPago(MedioPagoDTO mediPago) {
        this.mediPago = mediPago;
    }

    public TransportadorDTO getTransportador() {
        return transportador;
    }

    public void setTransportador(TransportadorDTO transportador) {
        this.transportador = transportador;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrdenDTO)) {
            return false;
        }

        OrdenDTO ordenDTO = (OrdenDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ordenDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrdenDTO{" +
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
            ", mediPago=" + getMediPago() +
            ", transportador=" + getTransportador() +
            "}";
    }
}
