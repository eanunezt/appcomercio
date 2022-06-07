package com.comercio.productos.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.comercio.productos.domain.TipoProducto} entity.
 */
public class TipoProductoDTO implements Serializable {

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
        if (!(o instanceof TipoProductoDTO)) {
            return false;
        }

        TipoProductoDTO tipoProductoDTO = (TipoProductoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tipoProductoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoProductoDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", codigo='" + getCodigo() + "'" +
            "}";
    }
}
