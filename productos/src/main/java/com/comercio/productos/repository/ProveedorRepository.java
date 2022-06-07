package com.comercio.productos.repository;

import com.comercio.productos.domain.Proveedor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Proveedor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {}
