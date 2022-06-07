package com.comercio.productos.domain.enumeration;

/**
 * The EstadoProducto enumeration.
 */
public enum EstadoProducto {
    ACTIVO("A"),
    INACTIVO("I");

    private final String value;

    EstadoProducto(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
