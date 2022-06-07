package com.comercio.pedidos.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.comercio.pedidos.domain.Orden;

/**
 * Spring Data SQL repository for the Orden entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
	
	@Query("SELECT t FROM Orden t WHERE t.fechaEntregaEstimada BETWEEN ?1 AND ?2")
	List<Orden> findByfechaEntregaEstimadaBetween(Date start, Date end);
	
	
	
}
