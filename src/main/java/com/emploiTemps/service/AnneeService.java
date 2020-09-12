package com.emploiTemps.service;

import com.emploiTemps.domain.Annee;
import com.emploiTemps.dto.AnneeDto;
import com.emploiTemps.repository.AnneeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing Annee.
 */
@Service
@Transactional
public class AnneeService {

    private final Logger log = LoggerFactory.getLogger(AnneeService.class);

    @Autowired
    private AnneeRepository anneeRepository;


    /**
     *  Get all the annees.
     *
     *  @return the list of entities
     */

    @Transactional(readOnly = true)
    public List<AnneeDto> findAll() {
        log.debug("Request to get all Annees");
        List<Annee> annees = anneeRepository.findAll();
        List<AnneeDto> anneeDtos = new ArrayList<>();
        for(Annee annee: annees){
            AnneeDto anneeDto = new AnneeDto().createDTO(annee);
            anneeDtos.add(anneeDto);
        }
        return anneeDtos;
    }

}
