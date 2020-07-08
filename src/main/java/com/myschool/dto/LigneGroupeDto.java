package com.myschool.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * Created by medilox on 3/10/17.
 */
@Data
@NoArgsConstructor
public class LigneGroupeDto {
    private String groupe;
    private Double moyenne;
    //rang de l'eleve
    private Integer rang;
    private Double moyenneGenerale;
    private Integer totalCoef;
    //position dans l'affichage du bulletin
    private Integer rank;
    private Double totalNotes;
    private List<LigneMatiereDto> matieres;
    private List<SequenceDto> sequences;
}
