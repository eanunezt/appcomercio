package com.comercio.pedidos.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.comercio.pedidos.domain.MedioPago} entity.
 */
public class MedioPagoDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    private String codigo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedioPagoDTO)) {
            return false;
        }

        MedioPagoDTO medioPagoDTO = (MedioPagoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medioPagoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedioPagoDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", codigo='" + getCodigo() + "'" +
            "}";
    }
}
