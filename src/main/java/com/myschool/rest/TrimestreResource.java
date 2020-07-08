package com.myschool.rest;

import com.myschool.domain.User;
import com.myschool.dto.TrimestreDto;
import com.myschool.repository.UserRepository;
import com.myschool.security.SecurityUtils;
import com.myschool.service.TrimestreService;
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
 * REST controller for managing Trimestre.
 */
@CrossOrigin
@RestController
public class TrimestreResource {

    private final Logger log = LoggerFactory.getLogger(TrimestreResource.class);

    @Autowired
    private TrimestreService trimestreService;

    @Autowired
    private UserRepository userRepository;

    /**
     * POST  /trimestres : Create a new trimestre.
     *
     * @param trimestreDto the trimestre to create
     * @return the ResponseEntity with status 201 (Created) and with body the new trimestre, or with status 400 (Bad Request) if the trimestre has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/api/trimestres")
    public ResponseEntity<TrimestreDto> createTrimestre(@Valid @RequestBody TrimestreDto trimestreDto) throws URISyntaxException {
        log.debug("REST request to save Trimestre : {}", trimestreDto);
        if (trimestreDto.getId() != null) {
            return new ResponseEntity(new CustomErrorType("Unable to create. A trimestre with id " +
                    trimestreDto.getId() + " already exist."), HttpStatus.CONFLICT);
        }
        return trimestreService.save(trimestreDto);
    }

    /**
     * PUT  /trimestres : Updates an existing trimestre.
     *
     * @param trimestreDto the trimestre to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated trimestre,
     * or with status 400 (Bad Request) if the trimestre is not valid,
     * or with status 500 (Internal Server Error) if the trimestre couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/api/trimestres")
    public ResponseEntity<TrimestreDto> updateTrimestre(@Valid @RequestBody TrimestreDto trimestreDto) throws URISyntaxException {
        log.debug("REST request to update Trimestre : {}", trimestreDto);
        if (trimestreDto.getId() == null) {
            return createTrimestre(trimestreDto);
        }
        return trimestreService.update(trimestreDto);
    }


    @GetMapping("/api/trimestres-by-annee/{anneeId}")
    public List<TrimestreDto> getTrimestresByannee(@PathVariable Long anneeId)
            throws URISyntaxException {
        log.debug("REST request to get all trimestres");
        return trimestreService.findByAnneeId(anneeId);
    }
    /**
     * GET  /trimestres/:id : get the "id" trimestre.
     *
     * @param id the id of the trimestre to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the trimestre, or with status 404 (Not Found)
     */
    @GetMapping("/api/trimestres/{id}")
    public ResponseEntity<TrimestreDto> getTrimestre(@PathVariable Long id) {
        log.debug("REST request to get Trimestre : {}", id);
        TrimestreDto trimestreDto = trimestreService.findOne(id);
        return Optional.ofNullable(trimestreDto)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * DELETE  /trimestres/:id : delete the "id" trimestre.
     *
     * @param id the id of the trimestre to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/api/trimestres/{id}")
    public ResponseEntity<?> deleteTrimestre(@PathVariable Long id) {
        log.debug("REST request to delete Trimestre : {}", id);
        trimestreService.delete(id);
        return new ResponseEntity<TrimestreDto>(HttpStatus.NO_CONTENT);
    }
}
