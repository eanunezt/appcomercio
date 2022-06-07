package com.comercio.pedidos.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.comercio.pedidos.domain.ItemOrden} entity.
 */
public class ItemOrdenDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer item;

    @NotNull
    private Integer cantidad;

    @NotNull
    private Double valorUnidad;

    @NotNull
    private Double valor;

    @NotNull
    private String codProducto;

    @NotNull
    private String nomProdcuto;

    private OrdenDTO orden;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getValorUnidad() {
        return valorUnidad;
    }

    public void setValorUnidad(Double valorUnidad) {
        this.valorUnidad = valorUnidad;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getCodProducto() {
        return codProducto;
    }

    public void setCodProducto(String codProducto) {
        this.codProducto = codProducto;
    }

    public String getNomProdcuto() {
        return nomProdcuto;
    }

    public void setNomProdcuto(String nomProdcuto) {
        this.nomProdcuto = nomProdcuto;
    }

    public OrdenDTO getOrden() {
        return orden;
    }

    public void setOrden(OrdenDTO orden) {
        this.orden = orden;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemOrdenDTO)) {
            return false;
        }

        ItemOrdenDTO itemOrdenDTO = (ItemOrdenDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, itemOrdenDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemOrdenDTO{" +
            "id=" + getId() +
            ", item=" + getItem() +
            ", cantidad=" + getCantidad() +
            ", valorUnidad=" + getValorUnidad() +
            ", valor=" + getValor() +
            ", codProducto='" + getCodProducto() + "'" +
            ", nomProdcuto='" + getNomProdcuto() + "'" +
            ", orden=" + getOrden() +
            "}";
    }
}
