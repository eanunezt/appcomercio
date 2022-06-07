package com.comercio.clientes.domain;

import com.comercio.clientes.domain.enumeration.TipoDocumento;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Cliente.
 */
@Entity
@Table(name = "cliente")
public class Cliente implements Serializable {

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
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_dcoumento", nullable = false)
    private TipoDocumento tipoDcoumento;

    @NotNull
    @Column(name = "num_documento", nullable = false)
    private String numDocumento;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "num_telefono", nullable = false)
    private String numTelefono;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnoreProperties(value = { "cliente" }, allowSetters = true)
    private Set<Direccion> direcciones = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cliente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Cliente nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoDocumento getTipoDcoumento() {
        return this.tipoDcoumento;
    }

    public Cliente tipoDcoumento(TipoDocumento tipoDcoumento) {
        this.setTipoDcoumento(tipoDcoumento);
        return this;
    }

    public void setTipoDcoumento(TipoDocumento tipoDcoumento) {
        this.tipoDcoumento = tipoDcoumento;
    }

    public String getNumDocumento() {
        return this.numDocumento;
    }

    public Cliente numDocumento(String numDocumento) {
        this.setNumDocumento(numDocumento);
        return this;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getEmail() {
        return this.email;
    }

    public Cliente email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumTelefono() {
        return this.numTelefono;
    }

    public Cliente numTelefono(String numTelefono) {
        this.setNumTelefono(numTelefono);
        return this;
    }

    public void setNumTelefono(String numTelefono) {
        this.numTelefono = numTelefono;
    }

    public Set<Direccion> getDirecciones() {
        return this.direcciones;
    }

    public void setDirecciones(Set<Direccion> direccions) {
        if (this.direcciones != null) {
            this.direcciones.forEach(i -> i.setCliente(null));
        }
        if (direccions != null) {
            direccions.forEach(i -> i.setCliente(this));
        }
        this.direcciones = direccions;
    }

    public Cliente direcciones(Set<Direccion> direccions) {
        this.setDirecciones(direccions);
        return this;
    }

    public Cliente addDirecciones(Direccion direccion) {
        this.direcciones.add(direccion);
        direccion.setCliente(this);
        return this;
    }

    public Cliente removeDirecciones(Direccion direccion) {
        this.direcciones.remove(direccion);
        direccion.setCliente(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cliente)) {
            return false;
        }
        return id != null && id.equals(((Cliente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cliente{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", tipoDcoumento='" + getTipoDcoumento() + "'" +
            ", numDocumento='" + getNumDocumento() + "'" +
            ", email='" + getEmail() + "'" +
            ", numTelefono='" + getNumTelefono() + "'" +
            "}";
    }
}
