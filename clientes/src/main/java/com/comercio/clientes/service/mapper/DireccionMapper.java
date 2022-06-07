package com.comercio.clientes.service.mapper;

import com.comercio.clientes.domain.Direccion;
import com.comercio.clientes.service.dto.DireccionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Direccion} and its DTO {@link DireccionDTO}.
 */
@Mapper(componentModel = "spring", uses = { ClienteMapper.class })
public interface DireccionMapper extends EntityMapper<DireccionDTO, Direccion> {
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "id")
    DireccionDTO toDto(Direccion s);
}
