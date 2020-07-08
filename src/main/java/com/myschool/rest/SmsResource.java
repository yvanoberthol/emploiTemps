package com.myschool.rest;

import com.myschool.domain.Sms;
import com.myschool.dto.SmsDto;
import com.myschool.repository.SmsRepository;
import com.myschool.repository.UserRepository;
import com.myschool.security.SecurityUtils;
import com.myschool.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * REST controller for managing Sms.
 */
@RestController
@CrossOrigin
public class SmsResource {

    private final Logger log = LoggerFactory.getLogger(SmsResource.class);

    @Autowired
    private SmsService smsService;

    @Autowired
    private SmsRepository smsRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * POST  /sms : Create a new sms.
     *
     * @param smsDto the sms to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sms, or with status 400 (Bad Request) if the sms has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/api/sms")
    public ResponseEntity createSms(@Valid @RequestBody SmsDto smsDto) throws URISyntaxException, KeyManagementException, NoSuchAlgorithmException, UnsupportedEncodingException {
        log.debug("REST request to save Sms : {}", smsDto);
        //automatically set user to current user
        //smsDto.setUserId(userRepository.findByLoginOrEmail(SecurityUtils.getCurrentUserLogin()).getId());
        return smsService.save(smsDto);
    }

    @GetMapping("/api/sms")
    public Page<SmsDto> getAllSmss(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                     @RequestParam(name = "size", defaultValue = "5") Integer size,
                                     @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
                                     @RequestParam(name = "direction", defaultValue = "desc") String direction,
                                     @RequestParam(name = "createdDateFrom") String createdDateFrom,
                                     @RequestParam(name = "createdDateTo") String createdDateTo,
                                     @RequestParam(name = "sender", defaultValue = "") String sender,
                                     @RequestParam(name = "recipient", defaultValue = "") String recipient
    ) {
        log.debug("REST request to get smss");
        return smsService.findByUserIsCurrentUser(page, size, sortBy, direction, createdDateFrom, createdDateTo, sender, recipient);
    }

    /**
     * GET  /sms/:id : get the "id" sms.
     *
     * @param id the id of the sms to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sms, or with status 404 (Not Found)
     */
    @GetMapping("/api/sms/{id}")
    public ResponseEntity<SmsDto> getSms(@PathVariable Long id) {
        log.debug("REST request to get Sms : {}", id);

        SmsDto smsDto = smsService.findOne(id);
        return Optional.ofNullable(smsDto)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    /**
     * DELETE  /sms/:id : delete the "id" sms.
     *
     * @param id the id of the sms to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/api/sms/{id}")
    public ResponseEntity<?> deleteSms(@PathVariable Long id) {
        log.debug("REST request to delete Sms : {}", id);
        Sms sms = smsRepository.findOne(id);
        if(Optional.ofNullable(sms).isPresent()){
            /*
            if(!sms.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin())
                    && !sms.getUser().getEmail().equals(SecurityUtils.getCurrentUserLogin())
                    && !SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")){
                throw new AccessDeniedException("You should not do this !");
            }
            */
            smsService.delete(id);
        }
        return new ResponseEntity<SmsDto>(HttpStatus.NO_CONTENT);
    }

}
