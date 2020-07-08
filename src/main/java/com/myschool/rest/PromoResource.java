package com.myschool.rest;

import com.myschool.dto.PromoDto;
import com.myschool.service.PromoService;
import com.myschool.utils.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Promo.
 */
@CrossOrigin
@RestController
public class PromoResource {

    private final Logger log = LoggerFactory.getLogger(PromoResource.class);

    @Autowired
    private PromoService promoService;

    /**
     * POST  /promos : Create a new promo.
     *
     * @param promoDto the promo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new promo, or with status 400 (Bad Request) if the promo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/api/promos")
    public ResponseEntity<PromoDto> createPromo(@Valid @RequestBody PromoDto promoDto) throws URISyntaxException {
        log.debug("REST request to save Promo : {}", promoDto);
        if (promoDto.getId() != null) {
            return new ResponseEntity(new CustomErrorType("Unable to create. A promo with id " +
                    promoDto.getId() + " already exist."), HttpStatus.CONFLICT);
        }
        return promoService.save(promoDto);
    }

    /**
     * PUT  /promos : Updates an existing promo.
     *
     * @param promoDto the promo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated promo,
     * or with status 400 (Bad Request) if the promo is not valid,
     * or with status 500 (Internal Server Error) if the promo couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/api/promos")
    public ResponseEntity<PromoDto> updatePromo(@Valid @RequestBody PromoDto promoDto) throws URISyntaxException {
        log.debug("REST request to update Promo : {}", promoDto);
        if (promoDto.getId() == null) {
            return createPromo(promoDto);
        }
        return promoService.update(promoDto);
    }

    @GetMapping("/api/promos-by-annee/{anneeId}")
    public List<PromoDto> getPromoByAnnee(@PathVariable Long anneeId) {
        log.debug("REST request to get All Promos : {}", anneeId);
        return promoService.findByAnnee(anneeId);
    }


    @GetMapping("/api/promos-enabled-by-annee/{anneeId}")
    public List<PromoDto> getPromoEnabledByAnnee(@PathVariable Long anneeId) {
        log.debug("REST request to get All Promos : {}", anneeId);
        return promoService.findByAnnee(anneeId);
    }

    /**
     * GET  /promos/:id : get the "id" promo.
     *
     * @param id the id of the promo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the promo, or with status 404 (Not Found)
     */
    @GetMapping("/api/promos/{id}")
    public ResponseEntity<PromoDto> getPromo(@PathVariable Long id) {
        log.debug("REST request to get Promo : {}", id);
        PromoDto promoDto = promoService.findOne(id);
        return Optional.ofNullable(promoDto)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
}
