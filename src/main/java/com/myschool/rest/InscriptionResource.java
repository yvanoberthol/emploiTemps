package com.myschool.rest;

import com.myschool.domain.Inscription;
import com.myschool.domain.InscriptionPK;
import com.myschool.domain.Promo;
import com.myschool.dto.InscriptionDto;
import com.myschool.dto.InscriptionDto;
import com.myschool.repository.InscriptionRepository;
import com.myschool.repository.PromoRepository;
import com.myschool.repository.UserRepository;
import com.myschool.security.SecurityUtils;
import com.myschool.service.InscriptionService;
import com.myschool.utils.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
 * REST controller for managing Inscription.
 */
@CrossOrigin
@RestController
public class InscriptionResource {

    private final Logger log = LoggerFactory.getLogger(InscriptionResource.class);

    @Autowired
    private InscriptionService inscriptionService;

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PromoRepository promoRepository;

    /**
     * POST  /inscriptions : Create a new inscription.
     *
     * @param inscriptionDto the inscription to create
     * @return the ResponseEntity with status 201 (Created) and with body the new inscription, or with status 400 (Bad Request) if the inscription has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/api/inscriptions")
    public ResponseEntity<?> createInscription(@Valid @RequestBody InscriptionDto inscriptionDto) throws URISyntaxException {
        log.debug("REST request to save Inscription : {}", inscriptionDto);

        //if(inscriptionDto.getStudentId() == null || inscriptionDto.getPromoId() == null){return null;}

        if(inscriptionDto.getStudentId() != null){
            InscriptionPK inscriptionPK = new InscriptionPK(inscriptionDto.getStudentId(), inscriptionDto.getPromoId());

            Inscription inscription = inscriptionRepository.findByInscriptionPK(inscriptionPK);
            if(inscription != null){
                return new ResponseEntity(new CustomErrorType("L'étudiant sélectionné est déjà inscrit dans cette classe ! "), HttpStatus.CONFLICT);
            }

            Promo promo = promoRepository.findOne(inscriptionDto.getPromoId());
            if(promo != null ){
                inscription = inscriptionRepository.findByStudentIdAndAnneeId(inscriptionDto.getStudentId(), promo.getAnnee().getId());
                if(inscription != null){
                    return new ResponseEntity(new CustomErrorType("L'étudiant sélectionné est déjà inscrit dans une autre classe ! "), HttpStatus.CONFLICT);
                }
            }
        }

        //automatically set user to current user
        inscriptionDto.setStaffId(userRepository.findByLoginOrEmail(SecurityUtils.getCurrentUserLogin()).getId());
        return inscriptionService.save(inscriptionDto);
    }

    /**
     * PUT  /inscriptions : Updates an existing inscription.
     *
     * @param inscriptionDto the inscription to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated inscription,
     * or with status 400 (Bad Request) if the inscription is not valid,
     * or with status 500 (Internal Server Error) if the inscription couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @CrossOrigin
    @RequestMapping(value = "/api/inscriptions", method = RequestMethod.PUT, consumes = {"multipart/form-data"})
    @ResponseBody
    public ResponseEntity<?> updateInscription(@RequestPart("inscription") InscriptionDto inscriptionDto,
                                               @RequestParam(name="file", required=false) MultipartFile file) throws URISyntaxException, IOException {
        log.debug("REST request to update Inscription : {}", inscriptionDto);
        if(inscriptionDto.getStudentId() == null || inscriptionDto.getPromoId() == null){return null;}

        InscriptionPK inscriptionPK = new InscriptionPK(inscriptionDto.getStudentId(), inscriptionDto.getPromoId());

        Inscription inscription = inscriptionRepository.findByInscriptionPK(inscriptionPK);
        if(inscription != null){
            return inscriptionService.update(inscriptionDto, file);
        }
        return null;
    }


    /*@GetMapping("/api/inscriptions-by-annee/{anneeId}")
    public Page<InscriptionDto> getAllInscriptions(@PathVariable Long anneeId,
                                    @RequestParam(name = "page", defaultValue = "0") Integer page,
                                       @RequestParam(name = "size", defaultValue = "999999") Integer size,
                                       @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
                                       @RequestParam(name = "direction", defaultValue = "desc") String direction,
                                       @RequestParam(name = "createdDateFrom") String createdDateFrom,
                                       @RequestParam(name = "createdDateTo") String createdDateTo) {
        log.debug("REST request to get Inscriptions by store");
        return inscriptionService.findByAnnee(page, size, sortBy, direction, anneeId, createdDateFrom, createdDateTo);
    }*/

    @GetMapping("/api/inscriptions-by-annee/{anneeId}")
    public Page<InscriptionDto> getInscriptionsByAnnee(@PathVariable Long anneeId,
                                                       @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                       @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                       @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
                                                       @RequestParam(name = "direction", defaultValue = "desc") String direction,
                                                       @RequestParam(name = "name", defaultValue="") String name,
                                                       @RequestParam(name = "promo", defaultValue="0") Long promoId) {
        log.debug("REST request to get all inscriptions");
        return inscriptionService.findByAnnee(page, size, sortBy, direction, anneeId, name, promoId);
    }

    /*@GetMapping("/api/inscriptions-by-promo/{promoId}")
    public Page<InscriptionDto> getAllInscriptionsByPromo(@PathVariable Long promoId,
                                           @RequestParam(name = "page", defaultValue = "0") Integer page,
                                           @RequestParam(name = "size", defaultValue = "999999") Integer size,
                                           @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
                                           @RequestParam(name = "direction", defaultValue = "desc") String direction) {
        log.debug("REST request to get Inscriptions by promo");
        return inscriptionService.findByPromo(page, size, sortBy, direction, promoId);
    }*/

    @GetMapping("/api/inscriptions-by-promo/{promoId}")
    public List<InscriptionDto> getInscriptionsByPromo(@PathVariable Long promoId,
                                               @RequestParam(name = "name", defaultValue="") String name) {
        log.debug("REST request to get all students");
        return inscriptionService.findByPromo(promoId, name);
    }

    /*
    @GetMapping("/api/inscriptions-by-promo-with-attendances/{promoId}")
    public Page<InscriptionDto> getInscriptionsByPromoWithAttendances(@PathVariable Long promoId,
                                                          @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                          @RequestParam(name = "size", defaultValue = "999999") Integer size,
                                                          @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
                                                          @RequestParam(name = "direction", defaultValue = "desc") String direction,
                                                          @RequestParam(name = "edtEventId") Long edtEventId) {
        log.debug("REST request to get Inscriptions by promo");
        return inscriptionService.findByPromoWithAttendances(page, size, sortBy, direction, promoId, edtEventId);
    }
    */
    /**
     * GET  /inscriptions/:id : get the "id" inscription.
     *
     * @param studentId the id of the inscription to retrieve
     * @param promoId the id of the inscription to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the inscription, or with status 404 (Not Found)
     */
    @GetMapping("/api/inscriptions")
    public ResponseEntity<InscriptionDto> getInscription(@RequestParam(name = "studentId") Long studentId,
                                                         @RequestParam(name = "promoId") Long promoId) {
        log.debug("REST request to get Inscription : {}");
        InscriptionDto inscriptionDto = inscriptionService.findOne(studentId, promoId);
        return Optional.ofNullable(inscriptionDto)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @RequestMapping(value="/api/inscription-image", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getInscriptionImage(@RequestParam(name = "studentId") Long studentId,
                                      @RequestParam(name = "promoId") Long promoId) throws IOException {
        return inscriptionService.getImage(studentId, promoId);
    }

    /**
     * DELETE  /inscriptions/:id : delete the "id" inscription.
     *
     * @param studentId the id of the inscription to delete
     * @param promoId the id of the inscription to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/api/inscriptions")
    public ResponseEntity<?> deleteInscription(@RequestParam(name = "studentId") Long studentId,
                                               @RequestParam(name = "promoId") Long promoId) {
        log.debug("REST request to delete Inscription : {}");
        inscriptionService.delete(studentId, promoId);
        return new ResponseEntity<InscriptionDto>(HttpStatus.NO_CONTENT);
    }
}
