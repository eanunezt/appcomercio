package com.comercio.pedidos.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransportadorMapperTest {

    private TransportadorMapper transportadorMapper;

    @BeforeEach
    public void setUp() {
        transportadorMapper = new TransportadorMapperImpl();
    }
}
