package com.myschool.dto;

import com.myschool.domain.Annee;
import com.myschool.domain.Promo;
import lombok.Data;

/**
 * Created by medilox on 30/09/18.
 */
@Data
public class AnneeDto {

    private Long id;
    private String label;
    private int debut;
    private int fin;
    private Boolean active;
    private int nbClasses;
    private int nbStudents;
    private String typeEtablissement;

    public AnneeDto createDTO(Annee annee) {
        AnneeDto anneeDto = new AnneeDto();
        if(annee != null){
            anneeDto.setId(annee.getId());
            anneeDto.setDebut(annee.getDebut());
            anneeDto.setFin(annee.getFin());
            anneeDto.setTypeEtablissement(String.valueOf(annee.getTypeEtablissement()));
            anneeDto.setLabel(annee.toString());

            if(annee.getPromos() !=  null){
                int nbStudents = 0;
                for(Promo promo: annee.getPromos()){
                    anneeDto.setNbClasses(anneeDto.getNbClasses() + 1);
                    nbStudents += promo.getInscriptions().size();
                }
                anneeDto.setNbStudents(nbStudents);
            }

            anneeDto.setActive(annee.getActive());
            return anneeDto;
        }
        return null;
    }

}
