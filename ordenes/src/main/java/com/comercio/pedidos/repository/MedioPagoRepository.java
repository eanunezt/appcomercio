package com.comercio.pedidos.repository;

import com.comercio.pedidos.domain.MedioPago;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MedioPago entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedioPagoRepository extends JpaRepository<MedioPago, Long> {}
