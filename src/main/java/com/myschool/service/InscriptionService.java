package com.myschool.service;

import com.myschool.domain.*;
import com.myschool.dto.InscriptionDto;
import com.myschool.dto.InscriptionFormDto;
import com.myschool.dto.StudentDto;
import com.myschool.repository.*;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Inscription.
 */
@Service
@Transactional
public class InscriptionService {

    private final Logger log = LoggerFactory.getLogger(InscriptionService.class);

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PromoRepository promoRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;


    /*public Inscription addNewInscription(String name) {
        Inscription result = inscriptionRepository.save(new Inscription(name));
        return result;
    }*/

    /**
     * Save a inscription.
     *
     * @param inscriptionFormDto the entity to save
     * @return the persisted entity
     */
    public ResponseEntity<?> save(InscriptionFormDto inscriptionFormDto) {
        log.debug("Request to save Inscription : {}", inscriptionFormDto);

        Inscription inscription;

        if(inscriptionFormDto.getStudentId() != null){
            InscriptionPK inscriptionPK = new InscriptionPK(inscriptionFormDto.getStudentId(), inscriptionFormDto.getPromoId());
            inscription = new Inscription(inscriptionPK);
            Student student = studentRepository.findOne(inscriptionFormDto.getStudentId());
            inscription.setStudent(student);
        }
        else{
            //create new student
            StudentDto student = inscriptionFormDto.getStudent();
            Student newStudent = studentService.save(student);
            if(newStudent == null)
                return new ResponseEntity<>("Error while creating student", HttpStatus.BAD_REQUEST);
            InscriptionPK inscriptionPK = new InscriptionPK(newStudent.getId(), inscriptionFormDto.getPromoId());
            inscription = new Inscription(inscriptionPK);
            inscription.setStudent(newStudent);
        }

        //set created date;
        String pattern = "yyyy-MM-dd HH:mm";
        LocalDateTime datetime = new LocalDateTime();
        inscription.setCreatedDate(datetime.toString(pattern));

        if(inscriptionFormDto.getPromoId() != null){
            Promo promo = promoRepository.findOne(inscriptionFormDto.getPromoId());
            inscription.setPromo(promo);
            inscription.setMontantScolarite(promo.getMontantScolarite());
        }

        if(inscriptionFormDto.getStaffId() != null){
            User user = userRepository.findOne(inscriptionFormDto.getStaffId());
            if(user != null){
                inscription.setStaff(user);
            }
        }

        inscription.setSolde(inscription.getSolde() - inscription.getPromo().getMontantScolarite());
        Inscription result = inscriptionRepository.save(inscription);
        return new ResponseEntity<InscriptionDto>(new InscriptionDto().createDTO(result), HttpStatus.CREATED);
    }


    public ResponseEntity<InscriptionDto> update(InscriptionFormDto inscriptionFormDto) {
        log.debug("Request to save Inscription : {}", inscriptionFormDto);

        InscriptionPK inscriptionPK = new InscriptionPK(inscriptionFormDto.getStudentId(), inscriptionFormDto.getPromoId());
        Inscription inscription = inscriptionRepository.findByInscriptionPK(inscriptionPK);

        Inscription result = inscriptionRepository.save(inscription);
        return new ResponseEntity<InscriptionDto>(new InscriptionDto().createDTO(result), HttpStatus.CREATED);
    }


    /**
     *  Get all the inscriptions.
     *
     *  @return the list of entities
     */

    /*@Transactional(readOnly = true)
    public List<InscriptionDto> findAll() {
        log.debug("Request to get all Inscriptions");
        List<Inscription> inscriptions = inscriptionRepository.findAll();
        List<InscriptionDto> inscriptionDtos = new ArrayList<>();
        for(Inscription i: inscriptions){
            inscriptionDtos.add(new InscriptionDto().createDTO(i));
        }
        return inscriptionDtos;
    }*/

    /**
     *  Get all the inscriptions.
     *
     *  @return the list of entities
     */
    /*public Page<InscriptionDto> findByAnnee(Integer page,
                                  Integer size,
                                  String sortBy,
                                  String direction,
                                  Long anneeId,
                                  String createdDateFrom,
                                  String createdDateTo) {
        log.debug("Request to get all Inscriptions");
        //Pageable pageable = new PageRequest(page, size);

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime cdf = null;
        if(createdDateFrom!=null)
            cdf = LocalDateTime.parse(createdDateFrom, formatter);

        LocalDateTime cdt = null;
        if(createdDateTo!=null)
            cdt = LocalDateTime.parse(createdDateTo, formatter);

        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sortBy);

        Page<Inscription> inscriptions = inscriptionRepository.findByPromoAnneeIdAndCreatedDateBetween(anneeId, cdf, cdt, pageable);

        Page<InscriptionDto> inscriptionDtos = inscriptions.map(inscription -> new InscriptionDto().createDTO(inscription));
        return inscriptionDtos;
    }*/

    public Page<InscriptionDto> findByAnnee(Integer page, Integer size, String sortBy, String direction, Long anneeId, String studentName, Long promoId) {
        log.debug("Request to get all Inscriptions");

        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sortBy);

        Page<Inscription> inscriptions;

        if(promoId == 0)
            inscriptions = inscriptionRepository.findByPromoAnneeIdAndStudent(anneeId,"%"+studentName+"%", pageable);
        else
            inscriptions = inscriptionRepository.findByPromoId(promoId, "%"+studentName+"%", pageable);

        Page<InscriptionDto> inscriptionDtos = inscriptions.map(inscription -> new InscriptionDto().createDTO(inscription));
        return inscriptionDtos;
    }

    public Page<InscriptionDto> findByPromo(Integer page, Integer size, String sortBy, String direction, Long promoId) {
        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sortBy);

        Page<Inscription> inscriptions = inscriptionRepository.findByPromoId(promoId, pageable);

        Page<InscriptionDto> inscriptionDtos = inscriptions.map(inscription -> new InscriptionDto().createDTO(inscription));
        return inscriptionDtos;
    }

    /*
    public Page<InscriptionDto> findByPromoWithAttendances(Integer page, Integer size, String sortBy, String direction, Long promoId, Long edtEventId) {
        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sortBy);

        Page<Inscription> inscriptions = inscriptionRepository.findByPromoId(promoId, pageable);

        Page<InscriptionDto> inscriptionDtos = inscriptions.map(inscription -> {

            return new InscriptionDto().createDTOWithAttendance(inscription, true);
        });
        return inscriptionDtos;
    }
    */

    /**
     *  Get one inscription by id.
     *
     *  @param studentId the id of the entity
     *  @param promoId the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public InscriptionDto findOne(Long studentId, Long promoId) {
        log.debug("Request to get Inscription : {}");
        InscriptionPK inscriptionPK = new InscriptionPK(studentId, promoId);
        Inscription inscription = inscriptionRepository.findByInscriptionPK(inscriptionPK);
        return new InscriptionDto().createDTO(inscription);
    }

    /**
     *  Delete the  inscription by id.
     *
     * @param studentId
     * @param promoId the id of the entity
     */
    public void delete(Long studentId, Long promoId) {
        log.debug("Request to delete Inscription : {}");
        InscriptionPK inscriptionPK = new InscriptionPK(studentId, promoId);
        Inscription inscription = inscriptionRepository.findByInscriptionPK(inscriptionPK);
        if(Optional.ofNullable(inscription).isPresent()){
            inscription.setSolde(inscription.getSolde() + inscription.getMontantScolarite());
            for(Reglement reglement: inscription.getReglements()){
                inscription.setSolde(inscription.getSolde() - reglement.getAmount());
            }
            studentRepository.save(inscription.getStudent());
            inscriptionRepository.deleteByInscriptionPK(inscriptionPK);

            List<Student> students = studentRepository.findAll();
            for(Student student: students){
                if(isEmptyOrNull(student.getInscriptions()))
                    studentRepository.delete(student.getId());
            }

        }
    }

    private boolean isEmptyOrNull(List<Inscription> inscriptions) {
        if(inscriptions == null) return true;
        if(inscriptions.isEmpty()) return true;
        return false;
    }
}
