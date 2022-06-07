package com.comercio.pedidos.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.comercio.pedidos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedioPagoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedioPagoDTO.class);
        MedioPagoDTO medioPagoDTO1 = new MedioPagoDTO();
        medioPagoDTO1.setId(1L);
        MedioPagoDTO medioPagoDTO2 = new MedioPagoDTO();
        assertThat(medioPagoDTO1).isNotEqualTo(medioPagoDTO2);
        medioPagoDTO2.setId(medioPagoDTO1.getId());
        assertThat(medioPagoDTO1).isEqualTo(medioPagoDTO2);
        medioPagoDTO2.setId(2L);
        assertThat(medioPagoDTO1).isNotEqualTo(medioPagoDTO2);
        medioPagoDTO1.setId(null);
        assertThat(medioPagoDTO1).isNotEqualTo(medioPagoDTO2);
    }
}
