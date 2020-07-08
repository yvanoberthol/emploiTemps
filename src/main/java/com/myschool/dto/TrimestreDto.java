package com.myschool.dto;

import com.myschool.domain.Sequence;
import com.myschool.domain.Trimestre;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by medilox on 30/09/18.
 */
@Data
public class TrimestreDto {

    private Long id;
    private String name;
    private Integer rank;
    private Long anneeId;
    private List<SequenceDto> sequences;
    private List<MatiereDto> matieres;

    public TrimestreDto createDTO(Trimestre trimestre) {
        TrimestreDto trimestreDto = new TrimestreDto();
        if(trimestre != null){
            trimestreDto.setId(trimestre.getId());
            trimestreDto.setName(trimestre.getName());
            trimestreDto.setRank(trimestre.getRank());

            ArrayList<SequenceDto> sequenceDtos = new ArrayList<>();
            if (trimestre.getSequences() != null) {
                for (Sequence sequence : trimestre.getSequences()){
                    sequenceDtos.add(new SequenceDto().createDTO(sequence));
                }
            }
            trimestreDto.setSequences(sequenceDtos);

            if(trimestre.getAnnee() != null){
                trimestreDto.setAnneeId(trimestre.getAnnee().getId());
            }

            return trimestreDto;
        }
        return null;
    }
}
