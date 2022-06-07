package com.comercio.pedidos.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ItemOrdenMapperTest {

    private ItemOrdenMapper itemOrdenMapper;

    @BeforeEach
    public void setUp() {
        itemOrdenMapper = new ItemOrdenMapperImpl();
    }
}
