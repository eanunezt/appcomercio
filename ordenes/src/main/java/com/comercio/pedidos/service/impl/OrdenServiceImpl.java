package com.comercio.pedidos.service.impl;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.comercio.pedidos.domain.Orden;
import com.comercio.pedidos.excepciones.ValidacionExcepcion;
import com.comercio.pedidos.repository.OrdenRepository;
import com.comercio.pedidos.service.OrdenService;
import com.comercio.pedidos.service.dto.OrdenDTO;
import com.comercio.pedidos.service.mapper.OrdenMapper;

/**
 * Service Implementation for managing {@link Orden}.
 */
@Service
@Transactional
public class OrdenServiceImpl implements OrdenService {

    private final Logger log = LoggerFactory.getLogger(OrdenServiceImpl.class);

    private final OrdenRepository ordenRepository;

    private final OrdenMapper ordenMapper;

    public OrdenServiceImpl(OrdenRepository ordenRepository, OrdenMapper ordenMapper) {
        this.ordenRepository = ordenRepository;
        this.ordenMapper = ordenMapper;
    }

    @Override
    public OrdenDTO save(OrdenDTO ordenDTO) {
        log.debug("Request to save Orden : {}", ordenDTO);
        Orden orden = ordenMapper.toEntity(ordenDTO);
        orden = ordenRepository.save(orden);
        return ordenMapper.toDto(orden);
    }

    @Override
    public Optional<OrdenDTO> partialUpdate(OrdenDTO ordenDTO) {
        log.debug("Request to partially update Orden : {}", ordenDTO);

        return ordenRepository
            .findById(ordenDTO.getId())
            .map(existingOrden -> {
                ordenMapper.partialUpdate(existingOrden, ordenDTO);

                return existingOrden;
            })
            .map(ordenRepository::save)
            .map(ordenMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrdenDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ordens");
        return ordenRepository.findAll(pageable).map(ordenMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdenDTO> findOne(Long id) {
        log.debug("Request to get Orden : {}", id);
        return ordenRepository.findById(id).map(ordenMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Orden : {}", id);
        ordenRepository.deleteById(id);
    }

	@Override
	public List<OrdenDTO> findOrdenesPorFechaDeEntrega(String fechaEntrega) throws ValidacionExcepcion {
			//utilizo la fechaEntrega para calcular 
		//las fechas en un rango de las 00 horas a las 29 horas
		try {
			Date myDate1 = new SimpleDateFormat("dd-MM-yyyy").parse(fechaEntrega);
			Date myDate2 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse(fechaEntrega+" 23:59:59");
			
			return ordenRepository.
					findByfechaEntregaEstimadaBetween(myDate1, myDate2)
					.stream()
					.map(ordenMapper::toDto)
					.collect(Collectors.toList());
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.error("Error Consultando Ordenes Por Fecha De Entrega",e);
			throw new ValidacionExcepcion("Error con el formato de la fecha: "+fechaEntrega.toString());
		}
	}
}
