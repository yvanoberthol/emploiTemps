package com.emploiTemps.dto;

import com.emploiTemps.domain.Seance;
import lombok.Data;

import java.io.Serializable;

@Data
public class SeanceDto implements Serializable {

    private Long id;
    private String jour;
    private Long idCours;
    private String matiere;
    private Integer tauxHoraire;
    private String creneauHoraire;
    private String classe;
    private boolean enabled;

    public SeanceDto createDTO(Seance seance) {

        if(seance != null){
            SeanceDto seanceDto = new SeanceDto();
            seanceDto.setId(seance.getId());
            seanceDto.setJour(seance.getJour().toString());
            seanceDto.setIdCours(seance.getCours().getId());
            seanceDto.setMatiere(seance.getCours().getMatiereTeacher()
                    .getMatiere().getName());
            seanceDto.setTauxHoraire(seance.getCours().getMatiereTeacher()
                    .getMatiere().getTauxHoraire());
            seanceDto.setClasse(seance.getCours().getMatiereTeacher()
                    .getMatiere().getPromo().getClasse());

            seanceDto.setCreneauHoraire(seance.getCours().getCreneauHoraire().getCreneauString());
            seanceDto.setEnabled(seance.isEnabled());
            return seanceDto;
        }
        return null;
    }
}
