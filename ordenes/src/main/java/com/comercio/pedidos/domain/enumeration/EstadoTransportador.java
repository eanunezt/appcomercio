package com.comercio.pedidos.domain.enumeration;

/**
 * The EstadoTransportador enumeration.
 */
public enum EstadoTransportador {
    ACTIVO("A"),
    INACTIVO("I");

    private final String value;

    EstadoTransportador(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
