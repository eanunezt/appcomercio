package com.comercio.pedidos.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MedioPagoMapperTest {

    private MedioPagoMapper medioPagoMapper;

    @BeforeEach
    public void setUp() {
        medioPagoMapper = new MedioPagoMapperImpl();
    }
}
