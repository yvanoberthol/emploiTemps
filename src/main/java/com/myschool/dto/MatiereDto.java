package com.myschool.dto;

import com.myschool.domain.Matiere;
import lombok.Data;

/**
 * Created by medilox on 30/09/18.
 */
@Data
public class MatiereDto {

    private Long id;
    private String name;
    private String teacher;
    private Double coef;
    private Integer noteSur;
    private Long groupeId;
    private String groupe;
    private Long promoId;

    public MatiereDto createDTO(Matiere matiere) {
        MatiereDto matiereDto = new MatiereDto();

        if(matiere != null){
            matiereDto.setId(matiere.getId());
            matiereDto.setCoef(matiere.getCoef());
            matiereDto.setNoteSur(matiere.getNoteSur());
            matiereDto.setName(matiere.getName());
            matiereDto.setTeacher(matiere.getTeacher());

            if(matiere.getGroupe() != null){
                matiereDto.setGroupeId(matiere.getGroupe().getId());
                matiereDto.setGroupe(matiere.getGroupe().getName());

                if(matiere.getGroupe().getPromo() != null)
                matiereDto.setPromoId(matiere.getGroupe().getPromo().getId());
            }

        }
        return matiereDto;
    }

    public Matiere fromDTO(MatiereDto matiereDto){
        Matiere matiere = new Matiere();
        matiere.setCoef(matiereDto.getCoef());
        matiere.setName(matiereDto.getName());
        return matiere;
    }

}
