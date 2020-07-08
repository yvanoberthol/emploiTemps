package com.myschool.rest;

import com.myschool.dto.SmsTemplateDto;
import com.myschool.service.SmsTemplateService;
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
 * REST controller for managing SmsTemplate.
 */

@CrossOrigin
@RestController
public class SmsTemplateResource {

    private final Logger log = LoggerFactory.getLogger(SmsTemplateResource.class);

    @Autowired
    private SmsTemplateService smsTemplateService;

    /**
     * POST  /sms-templates : Create a new smsTemplate.
     *
     * @param smsTemplateDto the smsTemplate to create
     * @return the ResponseEntity with status 201 (Created) and with body the new smsTemplate, or with status 400 (Bad Request) if the smsTemplate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */

    @PostMapping("/api/sms-templates")
    public ResponseEntity<SmsTemplateDto> createSmsTemplate(@Valid @RequestBody SmsTemplateDto smsTemplateDto) throws URISyntaxException {
        log.debug("REST request to save SmsTemplate : {}", smsTemplateDto);
        if (smsTemplateDto.getId() != null) {
            return new ResponseEntity(new CustomErrorType("Unable to create. A smsTemplate with id " +
                    smsTemplateDto.getId() + " already exist."), HttpStatus.CONFLICT);
        }
        return smsTemplateService.save(smsTemplateDto);
    }

    /**
     * PUT  /sms-templates : Updates an existing smsTemplate.
     *
     * @param smsTemplateDto the smsTemplate to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated smsTemplate,
     * or with status 400 (Bad Request) if the smsTemplate is not valid,
     * or with status 500 (Internal Server Error) if the smsTemplate couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */

    @PutMapping("/api/sms-templates")
    public ResponseEntity<SmsTemplateDto> updateSmsTemplate(@Valid @RequestBody SmsTemplateDto smsTemplateDto) throws URISyntaxException {
        log.debug("REST request to update SmsTemplate : {}", smsTemplateDto);
        if (smsTemplateDto.getId() == null) {
            return createSmsTemplate(smsTemplateDto);
        }
        return smsTemplateService.update(smsTemplateDto);
    }

    /**
     * GET  /sms-templates : get all the smsTemplates.
    */

    @GetMapping("/api/sms-templates")
    public List<SmsTemplateDto> getAllSmsTemplates()
            throws URISyntaxException {
        log.debug("REST request to get all smsTemplates");
        return smsTemplateService.findAll();
    }

    /**
     * GET  /sms-templates/:id : get the "id" smsTemplate.
     *
     * @param id the id of the smsTemplate to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the smsTemplate, or with status 404 (Not Found)
     */

    @GetMapping("/api/sms-templates/{id}")
    public ResponseEntity<SmsTemplateDto> getSmsTemplate(@PathVariable Long id) {
        log.debug("REST request to get SmsTemplate : {}", id);
        SmsTemplateDto smsTemplateDto = smsTemplateService.findOne(id);
        return Optional.ofNullable(smsTemplateDto)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * DELETE  /sms-templates/:id : delete the "id" smsTemplate.
     *
     * @param id the id of the smsTemplate to delete
     * @return the ResponseEntity with status 200 (OK)
     */

    @DeleteMapping("/api/sms-templates/{id}")
    public ResponseEntity<?> deleteSmsTemplate(@PathVariable Long id) {
        log.debug("REST request to delete SmsTemplate : {}", id);
        smsTemplateService.delete(id);
        return new ResponseEntity<SmsTemplateDto>(HttpStatus.NO_CONTENT);
    }
}
