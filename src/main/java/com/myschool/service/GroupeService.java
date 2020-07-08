package com.myschool.service;

import com.myschool.domain.Groupe;
import com.myschool.domain.Matiere;
import com.myschool.domain.Promo;
import com.myschool.dto.GroupeDto;
import com.myschool.dto.MatiereDto;
import com.myschool.repository.GroupeRepository;
import com.myschool.repository.MatiereRepository;
import com.myschool.repository.PromoRepository;
import com.myschool.repository.PromoRepository;
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
 * Service Implementation for managing Groupe.
 */
@Service
@Transactional
public class GroupeService {

    private final Logger log = LoggerFactory.getLogger(GroupeService.class);

    @Autowired
    private PromoRepository promoRepository;

    @Autowired
    private GroupeRepository groupeRepository;

    @Autowired
    private MatiereRepository matiereRepository;

    /**
     * Save a groupe.
     *
     * @param groupeDto the entity to save
     * @return the persisted entity
     */
    public ResponseEntity<GroupeDto> save(GroupeDto groupeDto) {
        log.debug("Request to save Groupe : {}", groupeDto);

        Groupe groupe = new Groupe();

        groupe.setId(groupeDto.getId());
        groupe.setRank(groupeDto.getRank());
        groupe.setName(groupeDto.getName());

        if(groupeDto.getPromoId() != null){
            Promo promo = promoRepository.findOne(groupeDto.getPromoId());
            groupe.setPromo(promo);
        }

        Groupe result = groupeRepository.save(groupe);

        if(result != null){
            //create all subSubcat
            if(groupeDto.getMatieres() != null){
                for(MatiereDto matiereDto: groupeDto.getMatieres()){
                    Matiere subSubcat = new MatiereDto().fromDTO(matiereDto);
                    subSubcat.setGroupe(result);
                    matiereRepository.save(subSubcat);
                }
            }
        }
        return new ResponseEntity<GroupeDto>(new GroupeDto().createDTO(result), HttpStatus.CREATED);
    }


    public ResponseEntity<GroupeDto> update(GroupeDto groupeDto) {
        log.debug("Request to save Groupe : {}", groupeDto);

        Groupe groupe = groupeRepository.findOne(groupeDto.getId());

        groupe.setRank(groupeDto.getRank());
        groupe.setName(groupeDto.getName());

        if(groupeDto.getPromoId() != null){
            Promo promo = promoRepository.findOne(groupeDto.getPromoId());
            groupe.setPromo(promo);
        }

        Groupe result = groupeRepository.save(groupe);
        return new ResponseEntity<GroupeDto>(new GroupeDto().createDTO(result), HttpStatus.CREATED);
    }

    public List<GroupeDto> findyPromoId(Long promoId) {
        List<GroupeDto> groupeDtos = new ArrayList<>();
        List<Groupe> groupes = groupeRepository.findByPromoId(promoId);

        for (Groupe groupe : groupes){
            groupeDtos.add(new GroupeDto().createDTO(groupe));
        }

        return groupeDtos;
    }


    /**
     *  Get one promo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public GroupeDto findOne(Long id) {
        log.debug("Request to get Groupe : {}", id);
        Groupe groupe = groupeRepository.findOne(id);
        return new GroupeDto().createDTO(groupe);
    }


    /**
     *  Delete the  groupe by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Groupe : {}", id);
        Groupe groupe = groupeRepository.findOne(id);
        if(Optional.ofNullable(groupe).isPresent()){
            groupeRepository.delete(id);
        }
    }
}
