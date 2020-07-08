package com.myschool.dto;

import com.myschool.domain.Groupe;
import com.myschool.domain.Matiere;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by medilox on 30/09/18.
 */
@Data
public class GroupeDto {

    private Long id;
    private int rank;
    private String name;
    private Long promoId;
    private List<MatiereDto> matieres;

    public GroupeDto createDTO(Groupe groupe) {
        GroupeDto groupeDto = new GroupeDto();

        if(groupe != null){
            groupeDto.setId(groupe.getId());
            groupeDto.setRank(groupe.getRank());
            groupeDto.setName(groupe.getName());

            if(groupe.getPromo() != null){
                groupeDto.setPromoId(groupe.getPromo().getId());
            }

            List<MatiereDto> matieres = new ArrayList<>();
            if (groupe.getMatieres() != null) {
                for (Matiere matiere : groupe.getMatieres()){
                    matieres.add(new MatiereDto().createDTO(matiere));
                }
            }
            groupeDto.setMatieres(matieres);
        }
        return groupeDto;
    }
}
