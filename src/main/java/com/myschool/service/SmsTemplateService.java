package com.myschool.service;

import com.myschool.domain.*;
import com.myschool.dto.SmsTemplateDto;
import com.myschool.repository.AnneeRepository;
import com.myschool.repository.SmsTemplateRepository;
import com.myschool.repository.UserRepository;
import com.myschool.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing SmsTemplate.
 */

@Service
@Transactional
public class SmsTemplateService {

    private final Logger log = LoggerFactory.getLogger(SmsTemplateService.class);

    @Autowired
    private SmsTemplateRepository smsTemplateRepository;

    @Autowired
    private AnneeRepository anneeRepository;

    @Autowired
    private PromoService promoService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Save a smsTemplate.
     *
     * @param smsTemplateDto the entity to save
     * @return the persisted entity
     */

    public ResponseEntity<SmsTemplateDto> save(SmsTemplateDto smsTemplateDto) {
        log.debug("Request to save SmsTemplate : {}", smsTemplateDto);

        SmsTemplate smsTemplate = new SmsTemplate();

        smsTemplate.setId(smsTemplateDto.getId());
        smsTemplate.setTitle(smsTemplateDto.getTitle());
        smsTemplate.setMessage(smsTemplateDto.getMessage());

        SmsTemplate result = smsTemplateRepository.save(smsTemplate);
        return new ResponseEntity<SmsTemplateDto>(new SmsTemplateDto().createDTO(result), HttpStatus.CREATED);
    }


    public ResponseEntity<SmsTemplateDto> update(SmsTemplateDto smsTemplateDto) {
        log.debug("Request to save SmsTemplate : {}", smsTemplateDto);

        SmsTemplate smsTemplate = smsTemplateRepository.findOne(smsTemplateDto.getId());

        smsTemplate.setId(smsTemplateDto.getId());
        smsTemplate.setTitle(smsTemplateDto.getTitle());
        smsTemplate.setMessage(smsTemplateDto.getMessage());

        SmsTemplate result = smsTemplateRepository.save(smsTemplate);
        return new ResponseEntity<SmsTemplateDto>(new SmsTemplateDto().createDTO(result), HttpStatus.CREATED);
    }

/**
     *  Get all the smsTemplates.
     *
     *  @return the list of entities
     */


    @Transactional(readOnly = true)
    public List<SmsTemplateDto> findAll() {
        log.debug("Request to get all SmsTemplates");
        List<SmsTemplate> smsTemplates = smsTemplateRepository.findAll();
        List<SmsTemplateDto> smsTemplateDtos = new ArrayList<>();
        for(SmsTemplate c: smsTemplates){
            smsTemplateDtos.add(new SmsTemplateDto().createDTO(c));
        }
        return smsTemplateDtos;
    }

/**
     *  Get one smsTemplate by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */

    @Transactional(readOnly = true)
    public SmsTemplateDto findOne(Long id) {
        log.debug("Request to get SmsTemplate : {}", id);
        SmsTemplate smsTemplate = smsTemplateRepository.findOne(id);
        return new SmsTemplateDto().createDTO(smsTemplate);
    }

/**
     *  Delete the  smsTemplate by id.
     *
     *  @param id the id of the entity
     */

    public void delete(Long id) {
        log.debug("Request to delete SmsTemplate : {}", id);
        SmsTemplate smsTemplate = smsTemplateRepository.findOne(id);
        if(Optional.ofNullable(smsTemplate).isPresent()){
            smsTemplateRepository.delete(id);
        }
    }
}
