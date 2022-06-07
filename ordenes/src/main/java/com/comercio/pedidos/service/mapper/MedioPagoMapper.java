package com.comercio.pedidos.service.mapper;

import com.comercio.pedidos.domain.MedioPago;
import com.comercio.pedidos.service.dto.MedioPagoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MedioPago} and its DTO {@link MedioPagoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MedioPagoMapper extends EntityMapper<MedioPagoDTO, MedioPago> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MedioPagoDTO toDtoId(MedioPago medioPago);
}
