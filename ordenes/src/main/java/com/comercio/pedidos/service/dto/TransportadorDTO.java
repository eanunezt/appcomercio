package com.comercio.pedidos.service.dto;

import com.comercio.pedidos.domain.enumeration.EstadoTransportador;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.comercio.pedidos.domain.Transportador} entity.
 */
public class TransportadorDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    private String codigo;

    @NotNull
    private EstadoTransportador estado;

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

    public EstadoTransportador getEstado() {
        return estado;
    }

    public void setEstado(EstadoTransportador estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransportadorDTO)) {
            return false;
        }

        TransportadorDTO transportadorDTO = (TransportadorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transportadorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransportadorDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", codigo='" + getCodigo() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
