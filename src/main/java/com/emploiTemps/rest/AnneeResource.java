package com.emploiTemps.rest;

import com.emploiTemps.dto.AnneeDto;
import com.emploiTemps.service.AnneeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing Annee.
 */
@CrossOrigin
@RestController
public class AnneeResource {

    private final Logger log = LoggerFactory.getLogger(AnneeResource.class);

    @Autowired
    private AnneeService anneeService;

    @GetMapping("/api/annees")
    public List<AnneeDto> getAllAnnees()
            throws URISyntaxException {
        log.debug("REST request to get all annees");
        return anneeService.findAll();
    }
}
