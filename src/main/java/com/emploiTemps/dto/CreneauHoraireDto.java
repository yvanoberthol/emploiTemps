package com.emploiTemps.dto;


import com.emploiTemps.domain.CreneauHoraire;
import lombok.Data;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * Created by yvano.
 */
@Data
public class CreneauHoraireDto implements Serializable {

    private Long id;
    private String heureDebut;
    private String heureFin;

    public CreneauHoraireDto createDTO(CreneauHoraire creneauHoraire) {

        if(creneauHoraire != null){
            SimpleDateFormat sdf = new SimpleDateFormat();
            CreneauHoraireDto creneauHoraireDto = new CreneauHoraireDto();
            creneauHoraireDto.setId(creneauHoraire.getId());
            creneauHoraireDto.setHeureDebut(creneauHoraire.getHeureDebut().toString());
            creneauHoraireDto.setHeureFin(creneauHoraire.getHeureFin().toString());
            return creneauHoraireDto;
        }
        return null;
    }

    public String getCreneauString(){
        return heureDebut+" "+heureFin;
    }
}
