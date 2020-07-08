package com.myschool.rest;

import com.myschool.dto.MatiereDto;
import com.myschool.service.MatiereService;
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
 * REST controller for managing Matiere.
 */
@RestController
public class MatiereResource {

    private final Logger log = LoggerFactory.getLogger(MatiereResource.class);

    @Autowired
    private MatiereService matiereService;

    /**
     * POST  /matieres : Create a new matiere.
     *
     * @param matiereDto the matiere to create
     * @return the ResponseEntity with status 201 (Created) and with body the new matiere, or with status 400 (Bad Request) if the matiere has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/api/matieres")
    public ResponseEntity<MatiereDto> createMatiere(@Valid @RequestBody MatiereDto matiereDto) throws URISyntaxException {
        log.debug("REST request to save Matiere : {}", matiereDto);
        if (matiereDto.getId() != null) {
            return new ResponseEntity(new CustomErrorType("Unable to create. A matiere with id " +
                    matiereDto.getId() + " already exist."), HttpStatus.CONFLICT);
        }
        return matiereService.save(matiereDto);
    }

    /**
     * PUT  /matieres : Updates an existing matiere.
     *
     * @param matiereDto the matiere to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated matiere,
     * or with status 400 (Bad Request) if the matiere is not valid,
     * or with status 500 (Internal Server Error) if the matiere couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/api/matieres")
    public ResponseEntity<MatiereDto> updateMatiere(@Valid @RequestBody MatiereDto matiereDto) throws URISyntaxException {
        log.debug("REST request to update Matiere : {}", matiereDto);
        if (matiereDto.getId() == null) {
            return createMatiere(matiereDto);
        }
        return matiereService.update(matiereDto);
    }

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

    /**
     * DELETE  /matieres/:id : delete the "id" matiere.
     *
     * @param id the id of the matiere to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/api/matieres/{id}")
    public ResponseEntity<?> deleteMatiere(@PathVariable Long id) {
        log.debug("REST request to delete Matiere : {}", id);
        matiereService.delete(id);
        return new ResponseEntity<MatiereDto>(HttpStatus.NO_CONTENT);
    }
}
