package com.emploiTemps.rest;

import com.emploiTemps.dto.PromoDto;
import com.emploiTemps.service.PromoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing Promo.
 */
@CrossOrigin
@RestController
public class PromoResource {

    private final Logger log = LoggerFactory.getLogger(PromoResource.class);

    @Autowired
    private PromoService promoService;

    @GetMapping("/api/promos")
    public List<PromoDto> getPromos() {
        return promoService.findAll();
    }

}
