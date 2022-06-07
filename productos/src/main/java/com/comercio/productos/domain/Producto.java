package com.comercio.productos.domain;

import com.comercio.productos.domain.enumeration.EstadoProducto;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Producto.
 */
@Entity
@Table(name = "producto")
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "codigo", nullable = false)
    private String codigo;

    @NotNull
    @Column(name = "valor", nullable = false)
    private Double valor;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoProducto estado;

    /**
     * Tipo de producto
     */
    @OneToOne
    @JoinColumn(unique = true)
    private TipoProducto tipoProducto;

    /**
     * proveedo productos
     */
    @OneToOne
    @JoinColumn(unique = true)
    private Proveedor proveedor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Producto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Producto nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public Producto codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Double getValor() {
        return this.valor;
    }

    public Producto valor(Double valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public EstadoProducto getEstado() {
        return this.estado;
    }

    public Producto estado(EstadoProducto estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoProducto estado) {
        this.estado = estado;
    }

    public TipoProducto getTipoProducto() {
        return this.tipoProducto;
    }

    public void setTipoProducto(TipoProducto tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public Producto tipoProducto(TipoProducto tipoProducto) {
        this.setTipoProducto(tipoProducto);
        return this;
    }

    public Proveedor getProveedor() {
        return this.proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Producto proveedor(Proveedor proveedor) {
        this.setProveedor(proveedor);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Producto)) {
            return false;
        }
        return id != null && id.equals(((Producto) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Producto{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", codigo='" + getCodigo() + "'" +
            ", valor=" + getValor() +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
