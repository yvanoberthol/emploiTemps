package com.myschool.service;

import com.myschool.domain.*;
import com.myschool.dto.*;
import com.myschool.repository.AnneeRepository;
import com.myschool.repository.PromoRepository;
import com.myschool.repository.TrimestreRepository;
import com.myschool.repository.UserRepository;
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
 * Service Implementation for managing Annee.
 */
@Service
@Transactional
public class AnneeService {

    private final Logger log = LoggerFactory.getLogger(AnneeService.class);

    @Autowired
    private AnneeRepository anneeRepository;

    @Autowired
    private PromoService promoService;

    @Autowired
    private PromoRepository promoRepository;

    @Autowired
    private TrimestreRepository trimestreRepository;

    @Autowired
    private TrimestreService trimestreService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Save a annee.
     *
     * @param anneeDto the entity to save
     * @return the persisted entity
     */
    public ResponseEntity<AnneeDto> save(AnneeDto anneeDto) {
        log.debug("Request to save Annee : {}", anneeDto);

        Annee annee = new Annee();

        annee.setId(anneeDto.getId());
        annee.setDebut(anneeDto.getDebut());
        annee.setFin(anneeDto.getFin());
        annee.setActive(anneeDto.getActive());

        Annee result = anneeRepository.save(annee);
        if (result != null){
            List<Trimestre> trimestres = trimestreRepository.findByAnneeActiveTrue();
            for(Trimestre trimestre: trimestres){
                TrimestreDto trimestreDto = new TrimestreDto();
                trimestreDto.setName(trimestre.getName());
                trimestreDto.setAnneeId(result.getId());
                trimestreDto.setRank(trimestre.getRank());

                //add trimestre sequences
                ArrayList<SequenceDto> sequenceDtos = new ArrayList<>();
                for(Sequence sequence : trimestre.getSequences()){
                    sequenceDtos.add(new SequenceDto().createDTO(sequence));
                }
                trimestreDto.setSequences(sequenceDtos);
                trimestreService.save(trimestreDto);
            }

            //create year promos for each existing classe
            List<Promo> promos = promoRepository.findByAnneeActiveTrue();
            for (Promo promo : promos ){
                PromoDto promoDto = new PromoDto();
                promoDto.setClasse(promo.getClasse());
                promoDto.setProfPrincipal(promo.getProfPrincipal());
                promoDto.setAnneeId(result.getId());

                //add promo groupes
                ArrayList<GroupeDto> groupeDtos = new ArrayList<>();
                for(Groupe groupe : promo.getGroupes()){
                    groupeDtos.add(new GroupeDto().createDTO(groupe));
                }
                promoDto.setGroupes(groupeDtos);

                //add promo matieres
                ArrayList<MatiereDto> matiereDtos = new ArrayList<>();
                for(Matiere matiere : promo.getMatieres()){
                    matiereDtos.add(new MatiereDto().createDTO(matiere));
                }
                promoDto.setMatieres(matiereDtos);

                promoService.save(promoDto);
            }
        }
        return new ResponseEntity<AnneeDto>(new AnneeDto().createDTO(result), HttpStatus.CREATED);
    }


    public ResponseEntity<AnneeDto> update(AnneeDto anneeDto) {
        log.debug("Request to save Annee : {}", anneeDto);

        Annee annee = anneeRepository.findOne(anneeDto.getId());

        annee.setId(anneeDto.getId());
        annee.setDebut(anneeDto.getDebut());
        annee.setFin(anneeDto.getFin());
        annee.setActive(anneeDto.getActive());

        Annee result = anneeRepository.save(annee);
        return new ResponseEntity<AnneeDto>(new AnneeDto().createDTO(result), HttpStatus.CREATED);
    }


    /**
     *  Get all the annees.
     *
     *  @return the list of entities
     */

    @Transactional(readOnly = true)
    public List<AnneeDto> findAll() {
        log.debug("Request to get all Annees");
        List<Annee> annees = anneeRepository.findAll();
        List<AnneeDto> anneeDtos = new ArrayList<>();
        for(Annee annee: annees){
            AnneeDto anneeDto = new AnneeDto().createDTO(annee);
            //anneeDto.setNbTeachers(teacherRepository.findByAnnee(annee.getId()).size());
            anneeDtos.add(anneeDto);
        }
        return anneeDtos;
    }


    /**
     *  Get one annee by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public AnneeDto findOne(Long id) {
        log.debug("Request to get Annee : {}", id);
        Annee annee = anneeRepository.findOne(id);
        return new AnneeDto().createDTO(annee);
    }

    @Transactional(readOnly = true)
    public AnneeDto findActive() {
        Annee annee = anneeRepository.findActive();
        AnneeDto result = new AnneeDto().createDTO(annee);
        /*if(result != null){
            List<Teacher> teachers = teacherRepository.findByAnnee(result.getId());
            result.setNbTeachers(teachers.size());
        }*/
        return result;
    }

    public void setActive(Long id) {
        log.debug("Request to set active Annee : {}", id);
        Annee annee = anneeRepository.findOne(id);
        Annee anneeActive = anneeRepository.findActive();
        if(Optional.ofNullable(annee).isPresent()){
            if(anneeActive != null)
                anneeActive.setActive(false);
            annee.setActive(true);
            anneeRepository.save(annee);
        }
    }

    /**
     *  Delete the  annee by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Annee : {}", id);
        Annee annee = anneeRepository.findOne(id);
        if(Optional.ofNullable(annee).isPresent()){
            anneeRepository.delete(id);
        }
    }
}
