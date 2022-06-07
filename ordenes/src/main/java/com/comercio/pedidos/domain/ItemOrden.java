package com.comercio.pedidos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ItemOrden.
 */
@Entity
@Table(name = "item_orden")
public class ItemOrden implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "item", nullable = false)
    private Integer item;

    @NotNull
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull
    @Column(name = "valor_unidad", nullable = false)
    private Double valorUnidad;

    @NotNull
    @Column(name = "valor", nullable = false)
    private Double valor;

    @NotNull
    @Column(name = "cod_producto", nullable = false)
    private String codProducto;

    @NotNull
    @Column(name = "nom_prodcuto", nullable = false)
    private String nomProdcuto;

    @ManyToOne
    @JsonIgnoreProperties(value = { "mediPago", "transportador", "items" }, allowSetters = true)
    private Orden orden;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ItemOrden id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getItem() {
        return this.item;
    }

    public ItemOrden item(Integer item) {
        this.setItem(item);
        return this;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public ItemOrden cantidad(Integer cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getValorUnidad() {
        return this.valorUnidad;
    }

    public ItemOrden valorUnidad(Double valorUnidad) {
        this.setValorUnidad(valorUnidad);
        return this;
    }

    public void setValorUnidad(Double valorUnidad) {
        this.valorUnidad = valorUnidad;
    }

    public Double getValor() {
        return this.valor;
    }

    public ItemOrden valor(Double valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getCodProducto() {
        return this.codProducto;
    }

    public ItemOrden codProducto(String codProducto) {
        this.setCodProducto(codProducto);
        return this;
    }

    public void setCodProducto(String codProducto) {
        this.codProducto = codProducto;
    }

    public String getNomProdcuto() {
        return this.nomProdcuto;
    }

    public ItemOrden nomProdcuto(String nomProdcuto) {
        this.setNomProdcuto(nomProdcuto);
        return this;
    }

    public void setNomProdcuto(String nomProdcuto) {
        this.nomProdcuto = nomProdcuto;
    }

    public Orden getOrden() {
        return this.orden;
    }

    public void setOrden(Orden orden) {
        this.orden = orden;
    }

    public ItemOrden orden(Orden orden) {
        this.setOrden(orden);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemOrden)) {
            return false;
        }
        return id != null && id.equals(((ItemOrden) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemOrden{" +
            "id=" + getId() +
            ", item=" + getItem() +
            ", cantidad=" + getCantidad() +
            ", valorUnidad=" + getValorUnidad() +
            ", valor=" + getValor() +
            ", codProducto='" + getCodProducto() + "'" +
            ", nomProdcuto='" + getNomProdcuto() + "'" +
            "}";
    }
}
