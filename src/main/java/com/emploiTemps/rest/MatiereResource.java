package com.emploiTemps.rest;

import com.emploiTemps.dto.MatiereDto;
import com.emploiTemps.service.MatiereService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Matiere.
 */
@CrossOrigin
@RestController
public class MatiereResource {

    private final Logger log = LoggerFactory.getLogger(MatiereResource.class);

    @Autowired
    private MatiereService matiereService;

    /**
     * GET  /matieres : get all the matieres.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of matieres in body
     */
    @GetMapping("/api/matieres-by-promo/{promoId}")
    public List<MatiereDto> getMatieres(@PathVariable Long promoId) {
        log.debug("REST request to get Matieres");
        return matiereService.findByPromoId(promoId);
    }

    /**
     * GET  /matieres/:id : get the "id" matiere.
     *
     * @param id the id of the matiere to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the matiere, or with status 404 (Not Found)
     */
    @GetMapping("/api/matieres/{id}")
    public ResponseEntity<MatiereDto> getMatiere(@PathVariable Long id) {
        log.debug("REST request to get Matiere : {}", id);
        MatiereDto matiereDto = matiereService.findOne(id);
        return Optional.ofNullable(matiereDto)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
