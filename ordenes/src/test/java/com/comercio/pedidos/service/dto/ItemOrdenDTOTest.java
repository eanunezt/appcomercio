package com.comercio.pedidos.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.comercio.pedidos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemOrdenDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemOrdenDTO.class);
        ItemOrdenDTO itemOrdenDTO1 = new ItemOrdenDTO();
        itemOrdenDTO1.setId(1L);
        ItemOrdenDTO itemOrdenDTO2 = new ItemOrdenDTO();
        assertThat(itemOrdenDTO1).isNotEqualTo(itemOrdenDTO2);
        itemOrdenDTO2.setId(itemOrdenDTO1.getId());
        assertThat(itemOrdenDTO1).isEqualTo(itemOrdenDTO2);
        itemOrdenDTO2.setId(2L);
        assertThat(itemOrdenDTO1).isNotEqualTo(itemOrdenDTO2);
        itemOrdenDTO1.setId(null);
        assertThat(itemOrdenDTO1).isNotEqualTo(itemOrdenDTO2);
    }
}
