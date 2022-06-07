package com.comercio.pedidos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.comercio.pedidos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransportadorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transportador.class);
        Transportador transportador1 = new Transportador();
        transportador1.setId(1L);
        Transportador transportador2 = new Transportador();
        transportador2.setId(transportador1.getId());
        assertThat(transportador1).isEqualTo(transportador2);
        transportador2.setId(2L);
        assertThat(transportador1).isNotEqualTo(transportador2);
        transportador1.setId(null);
        assertThat(transportador1).isNotEqualTo(transportador2);
    }
}
