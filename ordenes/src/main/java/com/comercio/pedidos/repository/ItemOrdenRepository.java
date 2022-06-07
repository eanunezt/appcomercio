package com.comercio.pedidos.repository;

import com.comercio.pedidos.domain.ItemOrden;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ItemOrden entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemOrdenRepository extends JpaRepository<ItemOrden, Long> {}
