package com.emploiTemps.service;

import com.emploiTemps.domain.Matiere;
import com.emploiTemps.domain.Promo;
import com.emploiTemps.dto.MatiereDto;
import com.emploiTemps.repository.MatiereRepository;
import com.emploiTemps.repository.PromoRepository;
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
 * Service Implementation for managing Matiere.
 */
@Service
@Transactional
public class MatiereService {

    private final Logger log = LoggerFactory.getLogger(MatiereService.class);


    @Autowired
    private PromoRepository promoRepository;

    @Autowired
    private MatiereRepository matiereRepository;

    /**
     * Save a matiere.
     *
     * @param matiereDto the entity to save
     * @return the persisted entity
     */
    public ResponseEntity<MatiereDto> save(MatiereDto matiereDto) {
        log.debug("Request to save Matiere : {}", matiereDto);

        Matiere matiere = new Matiere();

        matiere.setId(matiereDto.getId());
        matiere.setCoef((double) 1);
        matiere.setNoteSur(20);

        if(matiereDto.getCoef() != null){
            matiere.setCoef(matiereDto.getCoef());
        }

        if(matiereDto.getNoteSur() != null){
            matiere.setNoteSur(matiereDto.getNoteSur());
            matiere.setCoef((matiereDto.getNoteSur()/20.0));
        }

        matiere.setName(matiereDto.getName());

        if(matiereDto.getPromoId() != null){
            Promo promo = promoRepository.findOne(matiereDto.getPromoId());
            matiere.setPromo(promo);
        }

        Matiere result = matiereRepository.save(matiere);
        return new ResponseEntity<>(new MatiereDto().createDTO(result), HttpStatus.CREATED);
    }


    public ResponseEntity<MatiereDto> update(MatiereDto matiereDto) {
        log.debug("Request to save Matiere : {}", matiereDto);

        Matiere matiere = matiereRepository.findOne(matiereDto.getId());

        matiere.setCoef((double) 1);
        matiere.setNoteSur(20);

        if(matiereDto.getCoef() != null){
            matiere.setCoef(matiereDto.getCoef());
        }

        matiere.setName(matiereDto.getName());

        if(matiereDto.getPromoId() != null){
            Promo promo = promoRepository.findOne(matiereDto.getPromoId());
            matiere.setPromo(promo);
        }

        Matiere result = matiereRepository.save(matiere);
        return new ResponseEntity<>(new MatiereDto().createDTO(result), HttpStatus.CREATED);
    }

    public List<MatiereDto> findByPromoId(Long promoId) {
        List<MatiereDto> matiereDtos = new ArrayList<>();
        //List<Matiere> matieres = matiereRepository.findByGroupePromoId(promoId);
        List<Matiere> matieres = matiereRepository.findByPromoId(promoId);

        for (Matiere matiere : matieres){
            matiereDtos.add(new MatiereDto().createDTO(matiere));
        }

        return matiereDtos;
    }

    /**
     *  Get one groupe by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public MatiereDto findOne(Long id) {
        log.debug("Request to get Matiere : {}", id);
        Matiere matiere = matiereRepository.findOne(id);
        return new MatiereDto().createDTO(matiere);
    }


    /**
     *  Delete the  matiere by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Matiere : {}", id);
        Matiere matiere = matiereRepository.findOne(id);
        if(Optional.ofNullable(matiere).isPresent()){
            matiereRepository.delete(id);
        }
    }
}
