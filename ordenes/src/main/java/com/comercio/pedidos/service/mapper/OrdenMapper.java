package com.comercio.pedidos.service.mapper;

import com.comercio.pedidos.domain.Orden;
import com.comercio.pedidos.service.dto.OrdenDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Orden} and its DTO {@link OrdenDTO}.
 */
@Mapper(componentModel = "spring", uses = { MedioPagoMapper.class, TransportadorMapper.class })
public interface OrdenMapper extends EntityMapper<OrdenDTO, Orden> {
    @Mapping(target = "mediPago", source = "mediPago", qualifiedByName = "id")
    @Mapping(target = "transportador", source = "transportador", qualifiedByName = "id")
    OrdenDTO toDto(Orden s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrdenDTO toDtoId(Orden orden);
}
