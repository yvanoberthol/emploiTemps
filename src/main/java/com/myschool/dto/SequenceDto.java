package com.myschool.dto;

import com.myschool.domain.Sequence;
import lombok.Data;

/**
 * Created by medilox on 30/09/18.
 */
@Data
public class SequenceDto {

    private Long id;
    private String name;
    private Integer rank;
    private Double weight;
    private Long trimestreId;
    private String trimestre;

    private Double moyenne;
    private Double moyenneGenerale;
    private Double totalNotes;
    private Integer rang;
    private Integer effectif;

    public SequenceDto createDTO(Sequence sequence) {
        SequenceDto sequenceDto = new SequenceDto();
        if(sequence != null){
            sequenceDto.setId(sequence.getId());
            sequenceDto.setName(sequence.getName());
            sequenceDto.setRank(sequence.getRank());
            sequenceDto.setWeight(sequence.getWeight());

            if(sequence.getTrimestre() != null){
                sequenceDto.setTrimestreId(sequence.getTrimestre().getId());
                sequenceDto.setTrimestre(sequence.getTrimestre().getName());
            }

            return sequenceDto;
        }
        return null;
    }

}
