package com.myschool.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * Created by medilox on 3/10/17.
 */
@Data
@NoArgsConstructor
public class LigneTrimestreDto {
    private Long trimestreId;
    private String trimestre;
    private String classe;
    private String profPrincipal;
    private Double moyenne;
    private Double moyenneGenerale;
    private Double highestMark;
    private Double lowestMark;
    private Integer totalNoteSur;
    private Integer totalCoef;
    private Double totalNotes;
    private Integer rang;
    private Integer effectif;
    private List<LigneGroupeDto> groupes;
    private List<LigneMatiereDto> matieres;
    private List<SequenceDto> sequences;
}
