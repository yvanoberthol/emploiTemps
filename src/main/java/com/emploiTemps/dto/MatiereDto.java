package com.emploiTemps.dto;

import com.emploiTemps.domain.Matiere;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by medilox on 30/09/18.
 */
@Data
public class MatiereDto implements Serializable {

    private Long id;
    private String name;
    private String teacher;
    private Double coef;
    private Integer tauxHoraire = 0;
    private Integer noteSur;
    private Long promoId;
    private String promoName;

    public MatiereDto createDTO(Matiere matiere) {

        if(matiere != null){
            MatiereDto matiereDto = new MatiereDto();
            matiereDto.setId(matiere.getId());
            matiereDto.setCoef(matiere.getCoef());
            matiereDto.setTauxHoraire(matiere.getTauxHoraire());
            matiereDto.setNoteSur(matiere.getNoteSur());
            matiereDto.setName(matiere.getName());

            if (matiere.getPromo() != null){
                matiereDto.setPromoId(matiere.getPromo().getId());
                matiereDto.setPromoName(matiere.getPromo().getClasse());
            }
            return matiereDto;
        }
        return null;
    }
}
