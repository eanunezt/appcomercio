package com.comercio.clientes.service.dto;

import com.comercio.clientes.domain.enumeration.TipoDocumento;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.comercio.clientes.domain.Cliente} entity.
 */
public class ClienteDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    private TipoDocumento tipoDcoumento;

    @NotNull
    private String numDocumento;

    @NotNull
    private String email;

    @NotNull
    private String numTelefono;

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

    public TipoDocumento getTipoDcoumento() {
        return tipoDcoumento;
    }

    public void setTipoDcoumento(TipoDocumento tipoDcoumento) {
        this.tipoDcoumento = tipoDcoumento;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumTelefono() {
        return numTelefono;
    }

    public void setNumTelefono(String numTelefono) {
        this.numTelefono = numTelefono;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClienteDTO)) {
            return false;
        }

        ClienteDTO clienteDTO = (ClienteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clienteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClienteDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", tipoDcoumento='" + getTipoDcoumento() + "'" +
            ", numDocumento='" + getNumDocumento() + "'" +
            ", email='" + getEmail() + "'" +
            ", numTelefono='" + getNumTelefono() + "'" +
            "}";
    }
}
