package com.comercio.pedidos.repository;

import com.comercio.pedidos.domain.Transportador;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Transportador entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransportadorRepository extends JpaRepository<Transportador, Long> {}
