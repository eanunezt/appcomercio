package com.comercio.pedidos.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.comercio.pedidos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransportadorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransportadorDTO.class);
        TransportadorDTO transportadorDTO1 = new TransportadorDTO();
        transportadorDTO1.setId(1L);
        TransportadorDTO transportadorDTO2 = new TransportadorDTO();
        assertThat(transportadorDTO1).isNotEqualTo(transportadorDTO2);
        transportadorDTO2.setId(transportadorDTO1.getId());
        assertThat(transportadorDTO1).isEqualTo(transportadorDTO2);
        transportadorDTO2.setId(2L);
        assertThat(transportadorDTO1).isNotEqualTo(transportadorDTO2);
        transportadorDTO1.setId(null);
        assertThat(transportadorDTO1).isNotEqualTo(transportadorDTO2);
    }
}
