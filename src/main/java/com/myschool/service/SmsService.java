package com.myschool.service;

import com.myschool.domain.Sms;
import com.myschool.domain.User;
import com.myschool.dto.SmsDto;
import com.myschool.repository.SmsRepository;
import com.myschool.repository.UserRepository;
import com.myschool.security.SecurityUtils;
import com.myschool.utils.CustomErrorType;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * Service Implementation for managing Sms.
 */
@Service
@Transactional
public class SmsService {

    private final Logger log = LoggerFactory.getLogger(SmsService.class);

    @Autowired
    private SmsRepository smsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EasySendService easySendService;


    /**
     * Save a sms.
     *
     * @param smsDto the entity to save
     * @return the persisted entity
     */
    public ResponseEntity save(SmsDto smsDto) throws NoSuchAlgorithmException, KeyManagementException, UnsupportedEncodingException {
        log.debug("Request to save Sms : {}", smsDto);
        /*User user = userRepository.findOne(smsDto.getUserId());
        if(user.getSolde() != null){
            if(smsDto.getRecipients().size() * smsDto.getNbPages() <= user.getSolde())
                return easySendService.send(smsDto);
            else
                return new ResponseEntity(new CustomErrorType("Solde insuffisant ! Il vous reste " + user.getSolde() + "sms !"), HttpStatus.CONFLICT);
        }
        return new ResponseEntity(new CustomErrorType("Solde insuffisant !"), HttpStatus.CONFLICT);
        */
        return easySendService.send(smsDto);
    }


    /**
     *  Get all the smss.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Sms> findAll(Pageable pageable) {
        log.debug("Request to get all Smss");
        Page<Sms> result = smsRepository.findAll(pageable);
        return result;
    }


    public Page<SmsDto> findByUserIsCurrentUser(Integer page, Integer size, String sortBy, String direction, String createdDateFrom, String createdDateTo, String sender, String recipient) {
        log.debug("Request to get all Users");

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime cdf = null;
        if(createdDateFrom!=null)
            cdf = LocalDateTime.parse(createdDateFrom, formatter);

        LocalDateTime cdt = null;
        if(createdDateTo!=null)
            cdt = LocalDateTime.parse(createdDateTo, formatter);

        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sortBy);

        Page<Sms> smss = smsRepository.findAll(cdf, cdt, "%"+sender+"%", "%"+recipient+"%",  pageable);

        Page<SmsDto> smsDtos = smss.map(sms -> new SmsDto().createDTO(sms));
        return smsDtos;
    }

    /**
     *  Get one sms by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public SmsDto findOne(Long id) {
        log.debug("Request to get Sms : {}", id);
        Sms sms = smsRepository.findOne(id);

        /*if(Optional.ofNullable(sms).isPresent()){
            if(!sms.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin())
                    && !sms.getUser().getEmail().equals(SecurityUtils.getCurrentUserLogin())
                    && !SecurityUtils.isCurrentUserInRole("ROLE_ADMIN")){
                throw new AccessDeniedException("You should not do this !");
            }
        }*/

        return new SmsDto().createDTO(sms);
    }

    /**
     *  Delete the  sms by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Sms : {}", id);
        Sms sms = smsRepository.findOne(id);
        smsRepository.delete(id);
    }
}
