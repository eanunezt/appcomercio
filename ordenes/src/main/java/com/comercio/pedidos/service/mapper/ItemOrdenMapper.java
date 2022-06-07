package com.comercio.pedidos.service.mapper;

import com.comercio.pedidos.domain.ItemOrden;
import com.comercio.pedidos.service.dto.ItemOrdenDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemOrden} and its DTO {@link ItemOrdenDTO}.
 */
@Mapper(componentModel = "spring", uses = { OrdenMapper.class })
public interface ItemOrdenMapper extends EntityMapper<ItemOrdenDTO, ItemOrden> {
    @Mapping(target = "orden", source = "orden", qualifiedByName = "id")
    ItemOrdenDTO toDto(ItemOrden s);
}
