package com.myschool.rest;

import com.myschool.dto.GroupeDto;
import com.myschool.service.GroupeService;
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
 * REST controller for managing Groupe.
 */
@RestController
public class GroupeResource {

    private final Logger log = LoggerFactory.getLogger(GroupeResource.class);

    @Autowired
    private GroupeService groupeService;

    /**
     * POST  /groupes : Create a new groupe.
     *
     * @param groupeDto the groupe to create
     * @return the ResponseEntity with status 201 (Created) and with body the new groupe, or with status 400 (Bad Request) if the groupe has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/api/groupes")
    public ResponseEntity<GroupeDto> createGroupe(@Valid @RequestBody GroupeDto groupeDto) throws URISyntaxException {
        log.debug("REST request to save Groupe : {}", groupeDto);
        if (groupeDto.getId() != null) {
            return new ResponseEntity(new CustomErrorType("Unable to create. A groupe with id " +
                    groupeDto.getId() + " already exist."), HttpStatus.CONFLICT);
        }
        return groupeService.save(groupeDto);
    }

    /**
     * PUT  /groupes : Updates an existing groupe.
     *
     * @param groupeDto the groupe to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated groupe,
     * or with status 400 (Bad Request) if the groupe is not valid,
     * or with status 500 (Internal Server Error) if the groupe couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/api/groupes")
    public ResponseEntity<GroupeDto> updateGroupe(@Valid @RequestBody GroupeDto groupeDto) throws URISyntaxException {
        log.debug("REST request to update Groupe : {}", groupeDto);
        if (groupeDto.getId() == null) {
            return createGroupe(groupeDto);
        }
        return groupeService.update(groupeDto);
    }
    
    /**
     * GET  /groupes : get all the groupes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of groupes in body
     */
    @GetMapping("/api/groupes-by-promo/{promoId}")
    public List<GroupeDto> getGroupes(@PathVariable Long promoId) {
        log.debug("REST request to get Groupes");
        return groupeService.findyPromoId(promoId);
    }

    /**
     * GET  /groupes/:id : get the "id" groupe.
     *
     * @param id the id of the groupe to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the groupe, or with status 404 (Not Found)
     */
    @GetMapping("/api/groupes/{id}")
    public ResponseEntity<GroupeDto> getGroupe(@PathVariable Long id) {
        log.debug("REST request to get Groupe : {}", id);
        GroupeDto groupeDto = groupeService.findOne(id);
        return Optional.ofNullable(groupeDto)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /groupes/:id : delete the "id" groupe.
     *
     * @param id the id of the groupe to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/api/groupes/{id}")
    public ResponseEntity<?> deleteGroupe(@PathVariable Long id) {
        log.debug("REST request to delete Groupe : {}", id);
        groupeService.delete(id);
        return new ResponseEntity<GroupeDto>(HttpStatus.NO_CONTENT);
    }
}
