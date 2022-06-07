package com.comercio.productos.service.mapper;

import com.comercio.productos.domain.Proveedor;
import com.comercio.productos.service.dto.ProveedorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Proveedor} and its DTO {@link ProveedorDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProveedorMapper extends EntityMapper<ProveedorDTO, Proveedor> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProveedorDTO toDtoId(Proveedor proveedor);
}
