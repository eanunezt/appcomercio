package com.comercio.productos.repository;

import com.comercio.productos.domain.TipoProducto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TipoProducto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoProductoRepository extends JpaRepository<TipoProducto, Long> {}
