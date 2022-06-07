package com.comercio.pedidos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.comercio.pedidos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemOrdenTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemOrden.class);
        ItemOrden itemOrden1 = new ItemOrden();
        itemOrden1.setId(1L);
        ItemOrden itemOrden2 = new ItemOrden();
        itemOrden2.setId(itemOrden1.getId());
        assertThat(itemOrden1).isEqualTo(itemOrden2);
        itemOrden2.setId(2L);
        assertThat(itemOrden1).isNotEqualTo(itemOrden2);
        itemOrden1.setId(null);
        assertThat(itemOrden1).isNotEqualTo(itemOrden2);
    }
}
