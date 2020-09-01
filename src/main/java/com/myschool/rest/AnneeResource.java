package com.myschool.rest;

import com.myschool.dto.AnneeDto;
import com.myschool.service.AnneeService;
import com.myschool.utils.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Annee.
 */
@CrossOrigin
@RestController
public class AnneeResource {

    private final Logger log = LoggerFactory.getLogger(AnneeResource.class);

    @Autowired
    private AnneeService anneeService;

    /**
     * POST  /annees : Create a new annee.
     *
     * @param anneeDto the annee to create
     * @return the ResponseEntity with status 201 (Created) and with body the new annee, or with status 400 (Bad Request) if the annee has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/api/annees")
    public ResponseEntity<AnneeDto> createAnnee(@Valid @RequestBody AnneeDto anneeDto) throws URISyntaxException {
        log.debug("REST request to save Annee : {}", anneeDto);
        if (anneeDto.getId() != null) {
            return new ResponseEntity(new CustomErrorType("Unable to create. A annee with id " +
                    anneeDto.getId() + " already exist."), HttpStatus.CONFLICT);
        }
        return anneeService.save(anneeDto);
    }

    /**
     * PUT  /annees : Updates an existing annee.
     *
     * @param anneeDto the annee to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated annee,
     * or with status 400 (Bad Request) if the annee is not valid,
     * or with status 500 (Internal Server Error) if the annee couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/api/annees", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<AnneeDto> updateAnnee(@RequestPart("annee") AnneeDto anneeDto,
                                            @RequestPart(name="logo", required=false) MultipartFile logo,
                                            @RequestPart(name="cachet", required=false) MultipartFile cachet) throws IOException {
        log.debug("REST request to update Annee : {}", anneeDto);
        return anneeService.update(anneeDto, logo, cachet);
    }


    @GetMapping("/api/annees")
    public List<AnneeDto> getAllAnnees()
            throws URISyntaxException {
        log.debug("REST request to get all annees");
        return anneeService.findAll();
    }
    /**
     * GET  /annees/:id : get the "id" annee.
     *
     * @param id the id of the annee to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the annee, or with status 404 (Not Found)
     */
    @GetMapping("/api/annees/{id}")
    public ResponseEntity<AnneeDto> getAnnee(@PathVariable Long id) {
        log.debug("REST request to get Annee : {}", id);
        AnneeDto anneeDto = anneeService.findOne(id);
        return Optional.ofNullable(anneeDto)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/api/annee-active")
    public ResponseEntity<AnneeDto> getAnneeActive() {
        log.debug("REST request to get Annee active");
        AnneeDto anneeDto = anneeService.findActive();
        return Optional.ofNullable(anneeDto)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/api/annee-set-active/{id}")
    public ResponseEntity<?> setActive(@PathVariable Long id) {
        log.debug("REST request to set active Annee : {}", id);
        anneeService.setActive(id);
        return new ResponseEntity<AnneeDto>(HttpStatus.OK);
    }

    @GetMapping("/api/annee-update-student-card/{anneeId}/{studentCardId}")
    public ResponseEntity<?> updateCarteScolaire(@PathVariable Long anneeId, @PathVariable Long studentCardId) {
        log.debug("REST request to set active Annee : {}", anneeId);
        anneeService.updateCarteScolaire(anneeId, studentCardId);
        return new ResponseEntity<AnneeDto>(HttpStatus.OK);
    }

    /**
     * DELETE  /annees/:id : delete the "id" annee.
     *
     * @param id the id of the annee to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/api/annees/{id}")
    public ResponseEntity<?> deleteAnnee(@PathVariable Long id) {
        log.debug("REST request to delete Annee : {}", id);
        anneeService.delete(id);
        return new ResponseEntity<AnneeDto>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value="/api/annee-image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImage(@PathVariable("id") Long id,
                              @RequestParam(name = "name", defaultValue = "logo") String name) throws IOException {
        return anneeService.getImage(id, name);
    }
}
