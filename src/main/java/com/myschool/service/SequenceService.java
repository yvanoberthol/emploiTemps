package com.myschool.service;

import com.myschool.domain.Sequence;
import com.myschool.domain.Trimestre;
import com.myschool.dto.SequenceDto;
import com.myschool.repository.SequenceRepository;
import com.myschool.repository.TrimestreRepository;
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
 * Service Implementation for managing Sequence.
 */
@Service
@Transactional
public class SequenceService {

    private final Logger log = LoggerFactory.getLogger(SequenceService.class);

    @Autowired
    private SequenceRepository sequenceRepository;

    @Autowired
    private TrimestreRepository trimestreRepository;

    public Sequence addNewSequence(String name, int rank) {
        Sequence result = sequenceRepository.save(new Sequence(name, rank));
        return result;
    }

    /**
     * Save a sequence.
     *
     * @param sequenceDto the entity to save
     * @return the persisted entity
     */
    public ResponseEntity<SequenceDto> save(SequenceDto sequenceDto) {
        log.debug("Request to save Sequence : {}", sequenceDto);

        Sequence sequence = new Sequence();

        sequence.setId(sequenceDto.getId());
        sequence.setName(sequenceDto.getName());
        sequence.setWeight(sequenceDto.getWeight());

        if(sequenceDto.getTrimestreId() != null){
            Trimestre trimestre = trimestreRepository.findOne(sequenceDto.getTrimestreId());
            sequence.setTrimestre(trimestre);
        }

        Sequence result = sequenceRepository.save(sequence);
        return new ResponseEntity<SequenceDto>(new SequenceDto().createDTO(result), HttpStatus.CREATED);
    }


    public ResponseEntity<SequenceDto> update(SequenceDto sequenceDto) {
        log.debug("Request to save Sequence : {}", sequenceDto);

        Sequence sequence = sequenceRepository.findOne(sequenceDto.getId());

        sequence.setId(sequenceDto.getId());
        sequence.setName(sequenceDto.getName());
        sequence.setWeight(sequenceDto.getWeight());

        if(sequenceDto.getTrimestreId() != null){
            Trimestre trimestre = trimestreRepository.findOne(sequenceDto.getTrimestreId());
            sequence.setTrimestre(trimestre);
        }

        Sequence result = sequenceRepository.save(sequence);
        return new ResponseEntity<SequenceDto>(new SequenceDto().createDTO(result), HttpStatus.CREATED);
    }


    /**
     *  Get all the sequences.
     *
     *  @return the list of entities
     */

    @Transactional(readOnly = true)
    public List<SequenceDto> findAll() {
        log.debug("Request to get all Sequences");
        List<Sequence> sequences = sequenceRepository.findAll();
        List<SequenceDto> sequenceDtos = new ArrayList<>();
        for(Sequence c: sequences){
            sequenceDtos.add(new SequenceDto().createDTO(c));
        }
        return sequenceDtos;
    }

    public List<SequenceDto> findByTrimestreId(Long trimestreId) {
        List<Sequence> sequences = sequenceRepository.findByTrimestreId(trimestreId);
        List<SequenceDto> sequenceDtos = new ArrayList<>();
        for(Sequence c: sequences){
            sequenceDtos.add(new SequenceDto().createDTO(c));
        }
        return sequenceDtos;
    }

    public List<SequenceDto> findForMatiere(Long promoId, Long matiereId) {
        List<Sequence> sequences = sequenceRepository.findAll();
        List<SequenceDto> sequenceDtos = new ArrayList<>();
        for(Sequence ni: sequences){
            SequenceDto sequenceDto = new SequenceDto().createDTO(ni);
            /*
            MatiereSequence pmni = matiereSequenceRepository
                    .findBySequenceIdAndMatierePromoIdAndMatiereMatiereId(ni.getId(), promoId, matiereId);
            if(pmni != null){
                sequenceDto.setWeight(pmni.getWeight());
                sequenceDto.setEnabledForMatiere(true);
            }
            */
            sequenceDtos.add(sequenceDto);
        }
        return sequenceDtos;
    }
    


    /**
     *  Get one sequence by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public SequenceDto findOne(Long id) {
        log.debug("Request to get Sequence : {}", id);
        Sequence sequence = sequenceRepository.findOne(id);
        return new SequenceDto().createDTO(sequence);
    }

    /**
     *  Delete the  sequence by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Sequence : {}", id);
        Sequence sequence = sequenceRepository.findOne(id);
        if(Optional.ofNullable(sequence).isPresent()){
            sequenceRepository.delete(id);
        }
    }

    public Sequence findByName(String name) {
        return sequenceRepository.findByName(name);
    }
}
