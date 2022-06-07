package com.comercio.pedidos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.comercio.pedidos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedioPagoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedioPago.class);
        MedioPago medioPago1 = new MedioPago();
        medioPago1.setId(1L);
        MedioPago medioPago2 = new MedioPago();
        medioPago2.setId(medioPago1.getId());
        assertThat(medioPago1).isEqualTo(medioPago2);
        medioPago2.setId(2L);
        assertThat(medioPago1).isNotEqualTo(medioPago2);
        medioPago1.setId(null);
        assertThat(medioPago1).isNotEqualTo(medioPago2);
    }
}
