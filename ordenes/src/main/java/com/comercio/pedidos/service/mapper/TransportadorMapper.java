package com.comercio.pedidos.service.mapper;

import com.comercio.pedidos.domain.Transportador;
import com.comercio.pedidos.service.dto.TransportadorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transportador} and its DTO {@link TransportadorDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TransportadorMapper extends EntityMapper<TransportadorDTO, Transportador> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TransportadorDTO toDtoId(Transportador transportador);
}
