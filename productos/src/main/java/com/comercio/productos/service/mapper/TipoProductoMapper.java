package com.comercio.productos.service.mapper;

import com.comercio.productos.domain.TipoProducto;
import com.comercio.productos.service.dto.TipoProductoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoProducto} and its DTO {@link TipoProductoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TipoProductoMapper extends EntityMapper<TipoProductoDTO, TipoProducto> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TipoProductoDTO toDtoId(TipoProducto tipoProducto);
}
