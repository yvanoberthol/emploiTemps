package com.emploiTemps.dto;

import com.emploiTemps.domain.Matiere;
import com.emploiTemps.domain.Promo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by medilox on 30/09/18.
 */
@Data
public class PromoDto implements Serializable {

    private Long id;
    private Long anneeId;
    private String annee;
    private String classe;
    private TeacherPrincipalDto teacherPrincipalDto;
    private double montantScolarite;
    private int capacite;
    private List<MatiereDto> matiereDtos = new ArrayList<>();

    public PromoDto createDTO(Promo promo) {

        if(promo != null){
            PromoDto promoDto = new PromoDto();
            promoDto.setId(promo.getId());
            promoDto.setMontantScolarite(promo.getMontantScolarite());
            promoDto.setCapacite(promo.getCapacite());
            promoDto.setClasse(promo.getClasse());

            if (promo.getTeacherPrincipal() != null){
                TeacherPrincipalDto teacherPrincipalDto =
                        new TeacherPrincipalDto().createDTO(promo.getTeacherPrincipal());
                promoDto.setTeacherPrincipalDto(teacherPrincipalDto);
            }

            if(promo.getAnnee() != null){
                promoDto.setAnneeId(promo.getAnnee().getId());
                promoDto.setAnnee(promo.getAnnee().getSessionYear());
            }

            if(promo.getMatieres() != null){
                for (Matiere matiere : promo.getMatieres()){
                    MatiereDto matiereDto = new MatiereDto().createDTO(matiere);
                    matiereDtos.add(matiereDto);
                }
            }
            promoDto.setMatiereDtos(matiereDtos);

            return promoDto;
        }
        return null;
    }
}

