package com.emploiTemps.service;

import com.emploiTemps.domain.Annee;
import com.emploiTemps.domain.Matiere;
import com.emploiTemps.domain.Promo;
import com.emploiTemps.domain.TeacherPrincipal;
import com.emploiTemps.dto.MatiereDto;
import com.emploiTemps.dto.PromoDto;
import com.emploiTemps.repository.AnneeRepository;
import com.emploiTemps.repository.PromoRepository;
import com.emploiTemps.repository.TeacherPrincipalRepository;
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
 * Service Implementation for managing Promo.
 */
@Service
@Transactional
public class PromoService {

    private final Logger log = LoggerFactory.getLogger(PromoService.class);

    @Autowired
    private AnneeRepository anneeRepository;

    @Autowired
    private PromoRepository promoRepository;

    @Autowired
    private TeacherPrincipalRepository teacherPrincipalRepository;


    /**
     * Save a promo.
     *
     * @param promoDto the entity to save
     * @return the persisted entity
     */
    public ResponseEntity<PromoDto> save(PromoDto promoDto) {
        log.debug("Request to save Promo : {}", promoDto);

        Promo promo = new Promo();

        promo.setClasse(promoDto.getClasse());
        promo.setMontantScolarite(promoDto.getMontantScolarite());
        promo.setCapacite(promoDto.getCapacite());

        TeacherPrincipal teacherPrincipal =
                teacherPrincipalRepository.getOne(promoDto.getTeacherPrincipalDto().getId());
        promo.setTeacherPrincipal(teacherPrincipal);

        Annee annee = anneeRepository.findOne(promoDto.getAnneeId());
        promo.setAnnee(annee);

        Promo result = promoRepository.save(promo);

        if(result != null){
            ArrayList<Matiere> matieres = new ArrayList<>();
            for(MatiereDto matiereDto : promoDto.getMatiereDtos()){
                Matiere matiere = new Matiere();
                matiere.setCoef(matiereDto.getCoef());
                matiere.setName(matiereDto.getName());
                matiere.setNoteSur(matiereDto.getNoteSur());
                matiere.setPromo(result);
                matieres.add(matiere);
            }
            promo.setMatieres(matieres);
        }
        return new ResponseEntity<>(new PromoDto().createDTO(result), HttpStatus.CREATED);
    }

    /**
     * Update a promo.
     *
     * @param promoDto the entity to update
     * @return the persisted entity
     */
    public ResponseEntity<PromoDto> update(PromoDto promoDto) {
        log.debug("Request to update Promo : {}", promoDto);

        //Promo promo = promoRepository.findByAnneeIdAndClasseId(promoDto.getAnneeId(), promoDto.getClasseId());
        Promo promo = promoRepository.findOne(promoDto.getId());
        promo.setClasse(promoDto.getClasse());
        promo.setMontantScolarite(promoDto.getMontantScolarite());
        promo.setCapacite(promoDto.getCapacite());

        TeacherPrincipal teacherPrincipal =
                teacherPrincipalRepository.getOne(promoDto.getTeacherPrincipalDto().getId());
        promo.setTeacherPrincipal(teacherPrincipal);

        promo.setTeacherPrincipal(teacherPrincipal);

        Promo result = promoRepository.save(promo);
        return new ResponseEntity<>(new PromoDto().createDTO(result), HttpStatus.CREATED);
    }

    /**
     *  Get one promo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public PromoDto findOne(Long id) {
        log.debug("Request to get Promo : {}", id);
        Promo promo = promoRepository.findOne(id);
        return new PromoDto().createDTO(promo);
    }


    public List<PromoDto> findAll() {
        List<PromoDto> promoDtos = new ArrayList<>();
        List<Promo> promos = promoRepository
                .findAll();

        for (Promo promo : promos)
            promoDtos.add(new PromoDto().createDTO(promo));

        return promoDtos;
    }

    public void delete(Long id) {
        log.debug("Request to delete Promo : {}", id);
        Promo promo = promoRepository.findOne(id);
        if(Optional.ofNullable(promo).isPresent()){
            promoRepository.delete(id);
        }
    }

}