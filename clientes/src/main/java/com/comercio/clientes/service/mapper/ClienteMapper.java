package com.comercio.clientes.service.mapper;

import com.comercio.clientes.domain.Cliente;
import com.comercio.clientes.service.dto.ClienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cliente} and its DTO {@link ClienteDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClienteMapper extends EntityMapper<ClienteDTO, Cliente> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClienteDTO toDtoId(Cliente cliente);
}
