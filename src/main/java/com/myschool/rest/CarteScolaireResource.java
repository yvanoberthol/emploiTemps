package com.myschool.rest;

import com.myschool.dto.CarteScolaireDto;
import com.myschool.service.CarteScolaireService;
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
 * REST controller for managing CarteScolaire.
 */
@CrossOrigin
@RestController
public class CarteScolaireResource {

    private final Logger log = LoggerFactory.getLogger(CarteScolaireResource.class);

    @Autowired
    private CarteScolaireService carteScolaireService;


    /**
     * POST  /student-cards : Create a new carteScolaire.
     *
     * @param carteScolaireDto the carteScolaire to create
     * @return the ResponseEntity with status 201 (Created) and with body the new carteScolaire, or with status 400 (Bad Request) if the carteScolaire has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @CrossOrigin
    @RequestMapping(value = "/api/student-cards", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @ResponseBody
    public ResponseEntity<?> saveCarteScolaire(@RequestPart("studentCard") CarteScolaireDto carteScolaireDto,
                                                 @RequestParam(name="recto", required=false) MultipartFile recto,
                                                 @RequestParam(name="verso", required=false) MultipartFile verso) throws URISyntaxException, IOException {
        log.debug("REST request to save CarteScolaire : {}", carteScolaireDto);
        return carteScolaireService.save(carteScolaireDto, recto, verso);
    }

    /**
     * PUT  /student-cards : Updates an existing carteScolaire.
     *
     * @param carteScolaireDto the carteScolaire to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated carteScolaire,
     * or with status 400 (Bad Request) if the carteScolaire is not valid,
     * or with status 500 (Internal Server Error) if the carteScolaire couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @CrossOrigin
    @RequestMapping(value = "/api/student-cards", method = RequestMethod.PUT, consumes = {"multipart/form-data"})
    @ResponseBody
    public ResponseEntity<?> updateCarteScolaire(@RequestPart("studentCard") CarteScolaireDto carteScolaireDto,
                                               @RequestParam(name="recto", required=false) MultipartFile recto,
                                               @RequestParam(name="verso", required=false) MultipartFile verso) throws URISyntaxException, IOException {
        log.debug("REST request to update CarteScolaire : {}", carteScolaireDto);

        return carteScolaireService.update(carteScolaireDto, recto, verso);
    }

    @GetMapping("/api/student-cards-by-annee/{anneeId}")
    public List<CarteScolaireDto> getAllCarteScolaires(@PathVariable Long anneeId)
            throws URISyntaxException {
        log.debug("REST request to get all carteScolaires");
        return carteScolaireService.findAll(anneeId);
    }
    

    /**
     * GET  /student-cards/:id : get the "id" carteScolaire.
     *
     * @param carteScolaireId the id of the carteScolaire to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the carteScolaire, or with status 404 (Not Found)
     */
    @GetMapping("/api/student-cards/{carteScolaireId}")
    public ResponseEntity<CarteScolaireDto> getCarteScolaire(@PathVariable Long carteScolaireId) {
        log.debug("REST request to get CarteScolaire : {}");
        CarteScolaireDto carteScolaireDto = carteScolaireService.findOne(carteScolaireId);
        return Optional.ofNullable(carteScolaireDto)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @RequestMapping(value="/api/student-card-image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getCarteScolaireImage(@PathVariable("id") Long carteScolaireId,
                                        @RequestParam(name = "name", defaultValue = "recto") String name) throws IOException {
        return carteScolaireService.getImage(carteScolaireId, name);
    }

}
