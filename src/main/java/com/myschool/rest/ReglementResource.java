package com.myschool.rest;

import com.myschool.domain.Student;
import com.myschool.dto.ReglementDto;
import com.myschool.repository.StudentRepository;
import com.myschool.repository.UserRepository;
import com.myschool.security.SecurityUtils;
import com.myschool.service.MailService;
import com.myschool.service.ReglementService;
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
import java.util.Optional;


/**
 * REST controller for managing Reglement.
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class ReglementResource {

    private final Logger log = LoggerFactory.getLogger(ReglementResource.class);

    @Autowired
    private ReglementService reglementService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MailService mailService;

    /**
     * POST  /reglements : Create a new reglement.
     *
     * @param reglementDto the reglement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reglement, or with status 400 (Bad Request) if the reglement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/reglements", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ReglementDto> createReglement(@RequestPart("reglement") ReglementDto reglementDto,
                                                    @RequestPart(name="file", required=false) MultipartFile file) throws IOException {

        log.debug("REST request to save Reglement : {}", reglementDto);
        if (reglementDto.getId() != null) {
            return new ResponseEntity(new CustomErrorType("Unable to create. A reglement with id " +
                    reglementDto.getId() + " already exist."), HttpStatus.CONFLICT);
        }

        //automatically set user to current user
        reglementDto.setStaffId(userRepository.findByLoginOrEmail(SecurityUtils.getCurrentUserLogin()).getId());
        return reglementService.save(reglementDto, file);
    }

    /**
     * PUT  /reglements : Updates an existing reglement.
     *
     * @param reglementDto the reglement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reglement,
     * or with status 400 (Bad Request) if the reglement is not valid,
     * or with status 500 (Internal Server Error) if the reglement couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/reglements", method = RequestMethod.PUT, consumes = {"multipart/form-data"})
    @ResponseBody
    public ResponseEntity<ReglementDto> updateReglement(@RequestPart("reglement") ReglementDto reglementDto,
                                                    @RequestParam(name="file", required=false) MultipartFile file) throws URISyntaxException, IOException {
        log.debug("REST request to update Reglement : {}", reglementDto);
        if (reglementDto.getId() == null) {
            return createReglement(reglementDto, file);
        }
        return reglementService.update(reglementDto, file);
    }


    @PutMapping("/reglements-payment-validated")
    public ResponseEntity<ReglementDto> updateReglementPaymentValidated(@Valid @RequestBody ReglementDto reglementDto) throws URISyntaxException {
        log.debug("REST request to update Reglement : {}", reglementDto);
        if (reglementDto.getId() == null) {
            return null;
        }
        ReglementDto result = reglementService.updatePaymentValidated(reglementDto);

        if(reglementDto.getPaymentValidated()){
            Student student = studentRepository.findOne(reglementDto.getStudentId());
            //mailService.sendReglementValidatedMail(student, result);
        }
        return new ResponseEntity<ReglementDto>(result, HttpStatus.CREATED);
    }

    /**
     * GET  /reglements : get all the reglements.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of reglements in body
     */
    /*
    @GetMapping("/reglements")
    public List<ReglementDto> getAllReglements() {
        log.debug("REST request to get all Reglements");
        return reglementService.findAll();
    }
    */

    @GetMapping("/reglements")
    public Page<ReglementDto> getAllReglements(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
                                                @RequestParam(name = "direction", defaultValue = "desc") String direction,
                                                @RequestParam(name = "createdDateFrom") String createdDateFrom,
                                                @RequestParam(name = "createdDateTo") String createdDateTo)
            throws URISyntaxException {
        log.debug("REST request to get a page of Reglements");
        Page<ReglementDto> pageReglements =  reglementService.findAll(page, size, sortBy, direction, createdDateFrom, createdDateTo);
        return pageReglements;
    }

    @GetMapping("/reglements-by-annee/{anneeId}")
    public Page<ReglementDto> getReglementsByAnnee(@PathVariable Long anneeId,
                                       @RequestParam(name = "page", defaultValue = "0") Integer page,
                                       @RequestParam(name = "size", defaultValue = "999999") Integer size,
                                       @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
                                       @RequestParam(name = "direction", defaultValue = "desc") String direction,
                                       @RequestParam(name = "createdDateFrom") String createdDateFrom,
                                       @RequestParam(name = "createdDateTo") String createdDateTo,
                                       @RequestParam(name = "promo", defaultValue="0") Long promoId) {
        log.debug("REST request to get Reglements by annee");
        return reglementService.findByAnnee(page, size, sortBy, direction, anneeId, createdDateFrom, createdDateTo, promoId);
    }


    /*@GetMapping("/reglements-by-user")
    public Page<ReglementDto> getAllReglementsByCurrentUser(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                     @RequestParam(name = "sortBy", defaultValue = "dateTo") String sortBy,
                                                     @RequestParam(name = "direction", defaultValue = "desc") String direction)
            throws URISyntaxException {
        log.debug("REST request to get a page of Reglements");
        Page<ReglementDto> pageReglements =  reglementService.findAllByCurrentUser(page, size, sortBy, direction);
        return pageReglements;
    }*/

    /**
     * GET  /reglements/:id : get the "id" reglement.
     *
     * @param id the id of the reglement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reglement, or with status 404 (Not Found)
     */
    @GetMapping("/reglements/{id}")
    public ResponseEntity<ReglementDto> getReglement(@PathVariable Long id) {
        log.debug("REST request to get Reglement : {}", id);
        ReglementDto reglementDto = reglementService.findOne(id);

        return Optional.ofNullable(reglementDto)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @RequestMapping(value="/reglement-justificatif/{id}", produces = {MediaType.APPLICATION_PDF_VALUE, MediaType.IMAGE_JPEG_VALUE})
    @ResponseBody
    public byte[] getReglementImage(@PathVariable("id") Long reglementId) throws IOException {
        return reglementService.getReglementJustificatif(reglementId);
    }


    /**
     * DELETE  /reglements/:id : delete the "id" reglement.
     *
     * @param id the id of the reglement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reglements/{id}")
    public ResponseEntity<?> deleteReglement(@PathVariable Long id) {
        log.debug("REST request to delete Reglement : {}", id);
        reglementService.delete(id);
        return new ResponseEntity<ReglementDto>(HttpStatus.NO_CONTENT);
    }
}
