package com.myschool.service;

import com.myschool.domain.Annee;
import com.myschool.domain.Sequence;
import com.myschool.domain.Trimestre;
import com.myschool.domain.User;
import com.myschool.dto.SequenceDto;
import com.myschool.dto.TrimestreDto;
import com.myschool.repository.AnneeRepository;
import com.myschool.repository.MatiereRepository;
import com.myschool.repository.TrimestreRepository;
import com.myschool.repository.UserRepository;
import com.myschool.security.SecurityUtils;
import com.myschool.repository.MatiereRepository;
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
 * Service Implementation for managing Trimestre.
 */
@Service
@Transactional
public class TrimestreService {

    private final Logger log = LoggerFactory.getLogger(TrimestreService.class);

    @Autowired
    private TrimestreRepository trimestreRepository;

    @Autowired
    private MatiereRepository matiereRepository;

    @Autowired
    private AnneeRepository anneeRepository;


    public Trimestre addNewTrimestre(String name, int rank) {
        Trimestre result = trimestreRepository.save(new Trimestre(name, rank));
        return result;
    }

    /**
     * Save a trimestre.
     *
     * @param trimestreDto the entity to save
     * @return the persisted entity
     */
    public ResponseEntity<TrimestreDto> save(TrimestreDto trimestreDto) {
        log.debug("Request to save Trimestre : {}", trimestreDto);

        Trimestre trimestre = new Trimestre();

        trimestre.setId(trimestreDto.getId());
        trimestre.setName(trimestreDto.getName());
        trimestre.setRank(trimestreDto.getRank());

        Annee annee = anneeRepository.findOne(trimestreDto.getAnneeId());
        trimestre.setAnnee(annee);

        Trimestre result = trimestreRepository.save(trimestre);
        /*if(result != null){
            ArrayList<Sequence> sequences = new ArrayList<>();
            for(SequenceDto sequenceDto : trimestreDto.getSequences()){
                Sequence sequence = new Sequence();
                sequence.setRank(sequenceDto.getRank());
                sequence.setName(sequenceDto.getName());
                sequence.setWeight(sequenceDto.getWeight());
                sequence.setTrimestre(result);
                sequences.add(sequence);
            }
            trimestre.setSequences(sequences);
        }*/
        return new ResponseEntity<TrimestreDto>(new TrimestreDto().createDTO(result), HttpStatus.CREATED);
    }


    public ResponseEntity<TrimestreDto> update(TrimestreDto trimestreDto) {
        log.debug("Request to save Trimestre : {}", trimestreDto);

        Trimestre trimestre = trimestreRepository.findOne(trimestreDto.getId());

        trimestre.setId(trimestreDto.getId());
        trimestre.setName(trimestreDto.getName());
        trimestre.setRank(trimestreDto.getRank());

        Trimestre result = trimestreRepository.save(trimestre);
        return new ResponseEntity<TrimestreDto>(new TrimestreDto().createDTO(result), HttpStatus.CREATED);
    }

    public List<TrimestreDto> findByAnneeId(Long anneeId) {
        log.debug("Request to get all Trimestres");
        List<Trimestre> trimestres = trimestreRepository.findByAnneeIdOrderByRankAsc(anneeId);
        List<TrimestreDto> trimestreDtos = new ArrayList<>();
        for(Trimestre trimestre: trimestres){
            TrimestreDto trimestreDto = new TrimestreDto().createDTO(trimestre);
            trimestreDtos.add(trimestreDto);
        }
        return trimestreDtos;
    }

    /**
     *  Get one trimestre by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TrimestreDto findOne(Long id) {
        log.debug("Request to get Trimestre : {}", id);
        Trimestre trimestre = trimestreRepository.findOne(id);
        return new TrimestreDto().createDTO(trimestre);
    }

    /**
     *  Delete the  trimestre by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Trimestre : {}", id);
        Trimestre trimestre = trimestreRepository.findOne(id);
        if(Optional.ofNullable(trimestre).isPresent()){
            trimestreRepository.delete(id);
        }
    }

    public Trimestre findByName(String name) {
        return trimestreRepository.findByName(name);
    }
}
