package com.myschool.service;

import com.myschool.domain.*;
import com.myschool.domain.enumerations.PaymentMethod;
import com.myschool.dto.ReglementDto;
import com.myschool.repository.*;
import com.myschool.security.SecurityUtils;
import com.myschool.utils.CustomErrorType;
import org.apache.commons.io.IOUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Service Implementation for managing Reglement.
 */
@Service
@Transactional
public class ReglementService {

    private final Logger log = LoggerFactory.getLogger(ReglementService.class);

    @Autowired
    private ReglementRepository reglementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private PromoRepository promoRepository;

    @Autowired
    private MailService mailService;

    @Value("${dir.reglements}")
    private String IMG_FOLDER;

    /**
     * Save a reglement.
     *
     * @param reglementDto the entity to save
     * @param file
     * @return the persisted entity
     */
    public ResponseEntity<ReglementDto> save(ReglementDto reglementDto, MultipartFile file) throws IOException {
        log.debug("Request to save Reglement : {}", reglementDto);

        Reglement reglement = new Reglement();

        reglement.setId(reglementDto.getId());
        reglement.setAmount(reglementDto.getAmount());
        reglement.setPaymentMethod(PaymentMethod.fromValue(reglementDto.getPaymentMethod()));
        reglement.setPaymentValidated(false);

        //set created date;
        String patternDate = "yyyy-MM-dd HH:mm";
        LocalDateTime date = new LocalDateTime();
        reglement.setCreatedDate(date.toString(patternDate));

        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");

        if(reglementDto.getPromoId() != null){
            Promo promo = promoRepository.findOne(reglementDto.getPromoId());
            Inscription inscription = inscriptionRepository.findByStudentIdAndAnneeId(reglementDto.getStudentId(), promo.getAnnee().getId());
            inscription.setSolde(inscription.getSolde() + reglement.getAmount());
            reglement.setInscription(inscription);
        }
        //Inscription inscription = inscriptionRepository.findOne(reglementDto.getInscriptionId());
        //Student student = studentRepository.findOne(reglementDto.getStudentId());
        //reglement.setStudent(student);

        if(reglementDto.getStaffId() != null){
            User user = userRepository.findOne(reglementDto.getStaffId());
            reglement.setStaff(user);
        }

        Reglement result = reglementRepository.save(reglement);
        if(result != null){
            if (!Files.exists(Paths.get(IMG_FOLDER))) {
                File reglements = new File(IMG_FOLDER);
                if(! reglements.mkdirs()) {
                    return new ResponseEntity(new CustomErrorType("Unable to create folder ${dir.images}"), HttpStatus.CONFLICT);
                }
            }

            if(file != null){
                if(!file.isEmpty()){
                    try {
                        file.transferTo(new File(IMG_FOLDER + result.getId()));
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        return new ResponseEntity(new CustomErrorType("Error while saving reglement image"), HttpStatus.NO_CONTENT);
                    }
                }
            }
        }
        return new ResponseEntity<ReglementDto>(new ReglementDto().createDTO(result), HttpStatus.CREATED);
    }

    public ResponseEntity<ReglementDto> update(ReglementDto reglementDto, MultipartFile file) {
        log.debug("Request to update Reglement : {}", reglementDto);

        Reglement reglement = reglementRepository.findOne(reglementDto.getId());

        if(reglementDto.getPromoId() != null){
            Promo promo = promoRepository.findOne(reglementDto.getPromoId());
            Inscription inscription = inscriptionRepository.findByStudentIdAndAnneeId(reglementDto.getStudentId(), promo.getAnnee().getId());
            inscription.setSolde(inscription.getSolde() - reglement.getAmount() + reglementDto.getAmount());
        }

        reglement.setAmount(reglementDto.getAmount());

        Reglement result = reglementRepository.save(reglement);
        if(result != null){
            if (!Files.exists(Paths.get(IMG_FOLDER))) {
                File reglements = new File(IMG_FOLDER);
                if(! reglements.mkdirs()) {
                    return new ResponseEntity(new CustomErrorType("Unable to create folder ${dir.images}"), HttpStatus.CONFLICT);
                }
            }

            if(file != null){
                if(!file.isEmpty()){
                    try {
                        file.transferTo(new File(IMG_FOLDER + result.getId()));
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        return new ResponseEntity(new CustomErrorType("Error while saving reglement image"), HttpStatus.NO_CONTENT);
                    }
                }
            }
        }
        return new ResponseEntity<ReglementDto>(new ReglementDto().createDTO(result), HttpStatus.CREATED);
    }

    @Secured(value = {"ROLE_ADMIN"})
    public ReglementDto updatePaymentValidated(ReglementDto reglementDto) {
        log.debug("Request to update Reglement : {}", reglementDto);

        Reglement reglement = reglementRepository.findOne(reglementDto.getId());
        if(reglement == null){
            return null;
        }

        //Student student = studentRepository.findOne(reglement.getStudent().getId());

        reglement.setPaymentValidated(reglementDto.getPaymentValidated());
        if(reglementDto.getPaymentValidated()){
            String pattern = "yyyy-MM-dd HH:mm";
            LocalDateTime now = new LocalDateTime();
            reglement.setPaymentValidatedDate(now.toString(pattern));

            //update student solde
            //student.updateSolde(reglement);
        }
        else{
            reglement.setPaymentValidatedDate(null);
        }

        //studentRepository.save(student);
        Reglement result = reglementRepository.save(reglement);
        return new ReglementDto().createDTO(result);
    }

    /**
     *  Get all the reglements.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ReglementDto> findAll(Integer page,
                                      Integer size,
                                      String sortBy,
                                      String direction,
                                      String createdDateFrom,
                                      String createdDateTo) {
        log.debug("Request to get all Reglements");

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime cdf = null;
        if(createdDateFrom!=null)
            cdf = LocalDateTime.parse(createdDateFrom, formatter);

        LocalDateTime cdt = null;
        if(createdDateTo!=null)
            cdt = LocalDateTime.parse(createdDateTo, formatter);

        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sortBy);

        Page<Reglement> reglements = reglementRepository.findAll(cdf, cdt, pageable);

        Page<ReglementDto> reglementDtos = reglements.map(reglement -> new ReglementDto().createDTO(reglement));
        return reglementDtos;
    }


    /*public Page<ReglementDto> findAllByCurrentUser(Integer page, Integer size, String sortBy, String direction) {
        log.debug("Request to get all Reglements");

        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sortBy);

        Page<Reglement> reglements = reglementRepository.findByUserIsCurrentUser(SecurityUtils.getCurrentUserLogin(), pageable);

        Page<ReglementDto> reglementDtos = reglements.map(reglement -> new ReglementDto().createDTO(reglement));
        return reglementDtos;
    }*/

    public Page<ReglementDto> findByAnnee(Integer page, Integer size, String sortBy, String direction,
                                      Long anneeId, String createdDateFrom, String createdDateTo, Long promoId) {
        log.debug("Request to get all Reglements");
        //Pageable pageable = new PageRequest(page, size);

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime cdf = null;
        if(createdDateFrom!=null)
            cdf = LocalDateTime.parse(createdDateFrom, formatter);

        LocalDateTime cdt = null;
        if(createdDateTo!=null)
            cdt = LocalDateTime.parse(createdDateTo, formatter);

        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sortBy);

        Page<Reglement> reglements;

        if(promoId == 0)
            reglements = reglementRepository.findByAnnee(anneeId, cdf, cdt, pageable);
        else
            reglements = reglementRepository.findByAnneeAndPromoId(anneeId, cdf, cdt, promoId, pageable);

        Page<ReglementDto> reglementDtos = reglements.map(reglement -> new ReglementDto().createDTO(reglement));
        return reglementDtos;
    }

    /**
     *  Get one reglement by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ReglementDto findOne(Long id) {
        log.debug("Request to get Reglement : {}", id);
        Reglement reglement = reglementRepository.findOne(id);
        /*
        if(Optional.ofNullable(reglement).isPresent()){
            if(!reglement.getStudent().getEmail().equals(SecurityUtils.getCurrentUserLogin())){
                throw new AccessDeniedException("You should not do this !");
            }
        }
        */
        return new ReglementDto().createDTO(reglement);
    }

    public byte[] getReglementJustificatif(Long reglementId) throws IOException {
        File f = new File(IMG_FOLDER + reglementId);
        if(f.exists() && !f.isDirectory()) {
            return IOUtils.toByteArray(new FileInputStream(f));
        }
        /*f = new File(IMG_FOLDER + "no_image.png");
        if(f.exists() && !f.isDirectory()) {
            return IOUtils.toByteArray(new FileInputStream(f));
        }*/
        return null;
    }



    /**
     *  Delete the  reglement by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Reglement : {}", id);
        Reglement reglement = reglementRepository.findOne(id);
        /*if(Optional.ofNullable(reglement).isPresent()){
            if(!reglement.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin())
                    && !reglement.getUser().getEmail().equals(SecurityUtils.getCurrentUserLogin())){
                throw new AccessDeniedException("You should not do this !");
            }
        }*/
        reglementRepository.delete(id);
    }
}
