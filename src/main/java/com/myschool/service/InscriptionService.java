package com.myschool.service;

import com.myschool.domain.*;
import com.myschool.dto.InscriptionDto;
import com.myschool.dto.InscriptionDto;
import com.myschool.dto.StudentDto;
import com.myschool.repository.*;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
    private AnneeRepository anneeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PromoRepository promoRepository;

    @Autowired
    private ReglementRepository reglementRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    @Value("${dir.myschool}")
    private String FOLDER;



    /*public Inscription addNewInscription(String name) {
        Inscription result = inscriptionRepository.save(new Inscription(name));
        return result;
    }*/

    /**
     * Save a inscription.
     *
     * @param inscriptionDto the entity to save
     * @return the persisted entity
     */
    public ResponseEntity<?> save(InscriptionDto inscriptionDto) {
        log.debug("Request to save Inscription : {}", inscriptionDto);

        Inscription inscription;

        if(inscriptionDto.getStudentId() != null){
            InscriptionPK inscriptionPK = new InscriptionPK(inscriptionDto.getStudentId(), inscriptionDto.getPromoId());
            inscription = new Inscription(inscriptionPK);
            Student student = studentRepository.findOne(inscriptionDto.getStudentId());
            studentService.update(inscriptionDto.getStudent());
            inscription.setStudent(student);
        }
        else{
            //create new student
            StudentDto student = inscriptionDto.getStudent();
            Student newStudent = studentService.save(student);
            if(newStudent == null)
                return new ResponseEntity<>("Error while creating student", HttpStatus.BAD_REQUEST);
            InscriptionPK inscriptionPK = new InscriptionPK(newStudent.getId(), inscriptionDto.getPromoId());
            inscription = new Inscription(inscriptionPK);
            inscription.setStudent(newStudent);
        }

        //set created date;
        String pattern = "yyyy-MM-dd HH:mm";
        LocalDateTime datetime = new LocalDateTime();
        inscription.setCreatedDate(datetime.toString(pattern));

        if(inscriptionDto.getPromoId() != null){
            Promo promo = promoRepository.findOne(inscriptionDto.getPromoId());
            inscription.setPromo(promo);
            inscription.setMontantScolarite(promo.getMontantScolarite());
        }

        if(inscriptionDto.getStaffId() != null){
            User user = userRepository.findOne(inscriptionDto.getStaffId());
            if(user != null){
                inscription.setStaff(user);
            }
        }

        inscription.setSolde(inscription.getSolde() - inscription.getPromo().getMontantScolarite());
        Inscription result = inscriptionRepository.save(inscription);
        return new ResponseEntity<InscriptionDto>(new InscriptionDto().createDTO(result), HttpStatus.CREATED);
    }


    public ResponseEntity<InscriptionDto> update(InscriptionDto inscriptionDto, MultipartFile file) {
        log.debug("Request to save Inscription : {}", inscriptionDto);

        InscriptionPK inscriptionPK = new InscriptionPK(inscriptionDto.getStudentId(), inscriptionDto.getPromoId());
        Inscription inscription = inscriptionRepository.findByInscriptionPK(inscriptionPK);
        studentService.update(inscriptionDto.getStudent());

        Inscription result = inscriptionRepository.save(inscription);

        if(result != null){
            if (!Files.exists(Paths.get(FOLDER))) {
                File folder = new File(FOLDER);
                if(! folder.mkdirs()) {
                    return new ResponseEntity(new CustomErrorType("Unable to create folder ${dir.myschool}"), HttpStatus.CONFLICT);
                }
            }

            if (!Files.exists(Paths.get(FOLDER + inscription.getPromo().getAnnee().getId()))) {
                File anneeFolder = new File(FOLDER + inscription.getPromo().getAnnee().getId());
                if (!anneeFolder.mkdir()) {
                    return new ResponseEntity(new CustomErrorType("Unable to create folder for this annee"), HttpStatus.CONFLICT);
                }
            }

            if(file != null){
                if(!file.isEmpty()){
                    try {
                        file.transferTo(new File(FOLDER + inscription.getPromo().getAnnee().getId() + "/" + inscriptionDto.getPromoId() + "_" + inscriptionDto.getStudentId() + ".png"));
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        return new ResponseEntity(new CustomErrorType("Error while saving inscription image"), HttpStatus.NO_CONTENT);
                    }
                }
            }
        }
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

    /*public Page<InscriptionDto> findByPromo(Integer page, Integer size, String sortBy, String direction, Long promoId) {
        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sortBy);

        Page<Inscription> inscriptions = inscriptionRepository.findByPromoId(promoId, pageable);

        Page<InscriptionDto> inscriptionDtos = inscriptions.map(inscription -> new InscriptionDto().createDTO(inscription));
        return inscriptionDtos;
    }*/

    public List<InscriptionDto> findByPromo( Long promoId, String studentName) {
        log.debug("Request to get all Students");

        List<Inscription> inscriptions = inscriptionRepository.findByPromoId(promoId, "%"+studentName+"%");

        List<InscriptionDto> inscriptionDtos = new ArrayList<>();
        for(Inscription inscription: inscriptions){
            inscriptionDtos.add(new InscriptionDto().createDTO(inscription));
        }
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
        //return new InscriptionDto().createDTO(inscription);
        InscriptionDto inscriptionDto = new InscriptionDto().createDTO(inscription);
        inscriptionDto.setStudent(studentService.findOne(studentId, inscription.getPromo().getAnnee().getId()));
        return inscriptionDto;
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

            inscriptionRepository.deleteByInscriptionPK(inscriptionPK);

            /*List<Student> students = studentRepository.findAll();
            for(Student student: students){
                if(isEmptyOrNull(student.getInscriptions()))
                    studentRepository.delete(student.getId());
            }*/

        }
    }

    private boolean isEmptyOrNull(List<Inscription> inscriptions) {
        if(inscriptions == null) return true;
        if(inscriptions.isEmpty()) return true;
        return false;
    }

    public byte[] getImage(Long studentId, Long promoId) throws IOException {

        Promo promo = promoRepository.findOne(promoId);

        if (promo == null) {
            return null;
        }

        File f = new File(FOLDER + promo.getAnnee().getId() + "/" + promoId + "_" + studentId + ".png");
        if(f.exists() && !f.isDirectory()) {
            return IOUtils.toByteArray(new FileInputStream(f));
        }
        return null;
    }
}
