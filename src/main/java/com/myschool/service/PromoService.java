package com.myschool.service;

import com.myschool.domain.*;
import com.myschool.dto.GroupeDto;
import com.myschool.dto.MatiereDto;
import com.myschool.dto.PromoDto;
import com.myschool.repository.*;
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
    private UserRepository userRepository;


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
        promo.setProfPrincipal(promoDto.getProfPrincipal());

        Annee annee = anneeRepository.findOne(promoDto.getAnneeId());
        promo.setAnnee(annee);

        Promo result = promoRepository.save(promo);

        if(result != null){
            ArrayList<Matiere> matieres = new ArrayList<>();
            for(MatiereDto matiereDto : promoDto.getMatieres()){
                Matiere matiere = new Matiere();
                matiere.setCoef(matiereDto.getCoef());
                matiere.setName(matiereDto.getName());
                matiere.setTeacher(matiereDto.getTeacher());
                matiere.setNoteSur(matiereDto.getNoteSur());
                matiere.setPromo(result);
                matieres.add(matiere);
            }
            promo.setMatieres(matieres);

            ArrayList<Groupe> groupes = new ArrayList<>();
            for(GroupeDto groupeDto : promoDto.getGroupes()){
                Groupe groupe = new Groupe();
                groupe.setRank(groupeDto.getRank());
                groupe.setName(groupeDto.getName());
                groupe.setPromo(result);
                groupes.add(groupe);
            }
            promo.setGroupes(groupes);
        }
        return new ResponseEntity<PromoDto>(new PromoDto().createDTO(result), HttpStatus.CREATED);
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
        promo.setProfPrincipal(promoDto.getProfPrincipal());

        Promo result = promoRepository.save(promo);
        return new ResponseEntity<PromoDto>(new PromoDto().createDTO(result), HttpStatus.CREATED);
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


    public List<PromoDto> findByAnnee(Long anneeId) {
        List<PromoDto> promoDtos = new ArrayList<>();
        List<Promo> promos = promoRepository
                .findByAnneeId(anneeId);

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