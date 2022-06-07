package com.comercio.productos.service.mapper;

import com.comercio.productos.domain.Producto;
import com.comercio.productos.service.dto.ProductoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Producto} and its DTO {@link ProductoDTO}.
 */
@Mapper(componentModel = "spring", uses = { TipoProductoMapper.class, ProveedorMapper.class })
public interface ProductoMapper extends EntityMapper<ProductoDTO, Producto> {
    @Mapping(target = "tipoProducto", source = "tipoProducto", qualifiedByName = "id")
    @Mapping(target = "proveedor", source = "proveedor", qualifiedByName = "id")
    ProductoDTO toDto(Producto s);
}
