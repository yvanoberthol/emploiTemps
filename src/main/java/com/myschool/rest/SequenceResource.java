package com.myschool.rest;

import com.myschool.dto.SequenceDto;
import com.myschool.service.SequenceService;
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
 * REST controller for managing Sequence.
 */
@CrossOrigin
@RestController
public class SequenceResource {

    private final Logger log = LoggerFactory.getLogger(SequenceResource.class);

    @Autowired
    private SequenceService sequenceService;

    /**
     * POST  /sequences : Create a new sequence.
     *
     * @param sequenceDto the sequence to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sequence, or with status 400 (Bad Request) if the sequence has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/api/sequences")
    public ResponseEntity<SequenceDto> createSequence(@Valid @RequestBody SequenceDto sequenceDto) throws URISyntaxException {
        log.debug("REST request to save Sequence : {}", sequenceDto);
        if (sequenceDto.getId() != null) {
            return new ResponseEntity(new CustomErrorType("Unable to create. A sequence with id " +
                    sequenceDto.getId() + " already exist."), HttpStatus.CONFLICT);
        }
        return sequenceService.save(sequenceDto);
    }

    /**
     * PUT  /sequences : Updates an existing sequence.
     *
     * @param sequenceDto the sequence to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sequence,
     * or with status 400 (Bad Request) if the sequence is not valid,
     * or with status 500 (Internal Server Error) if the sequence couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/api/sequences")
    public ResponseEntity<SequenceDto> updateSequence(@Valid @RequestBody SequenceDto sequenceDto) throws URISyntaxException {
        log.debug("REST request to update Sequence : {}", sequenceDto);
        if (sequenceDto.getId() == null) {
            return createSequence(sequenceDto);
        }
        return sequenceService.update(sequenceDto);
    }


    /**
     * GET  /sequences : get all the sequences.
    */
    @GetMapping("/api/sequences")
    public List<SequenceDto> getAllSequences()
            throws URISyntaxException {
        log.debug("REST request to get all sequences");
        return sequenceService.findAll();
    }

    @GetMapping("/api/sequences-by-trimestre/{trimestreId}")
    public List<SequenceDto> getSequencesByTrimestre(@PathVariable Long trimestreId)
            throws URISyntaxException {
        log.debug("REST request to get all sequences");
        return sequenceService.findByTrimestreId(trimestreId);
    }

    @GetMapping("/api/sequences-for-matiere")
    public List<SequenceDto> getSequencesForMatiere(@RequestParam Long promoId, @RequestParam Long matiereId)
            throws URISyntaxException {
        log.debug("REST request to get all sequences");
        return sequenceService.findForMatiere(promoId, matiereId);
    }
    /**
     * GET  /sequences/:id : get the "id" sequence.
     *
     * @param id the id of the sequence to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sequence, or with status 404 (Not Found)
     */
    @GetMapping("/api/sequences/{id}")
    public ResponseEntity<SequenceDto> getSequence(@PathVariable Long id) {
        log.debug("REST request to get Sequence : {}", id);
        SequenceDto sequenceDto = sequenceService.findOne(id);
        return Optional.ofNullable(sequenceDto)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * DELETE  /sequences/:id : delete the "id" sequence.
     *
     * @param id the id of the sequence to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/api/sequences/{id}")
    public ResponseEntity<?> deleteSequence(@PathVariable Long id) {
        log.debug("REST request to delete Sequence : {}", id);
        sequenceService.delete(id);
        return new ResponseEntity<SequenceDto>(HttpStatus.NO_CONTENT);
    }
}
