package com.emploiTemps.dto;


import com.emploiTemps.domain.Cours;
import com.emploiTemps.domain.Seance;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yvano.
 */
@Data
public class CoursDto implements Serializable {

    private Long id;
    private int jour;
    private Long creneauHoraireId;
    private CreneauHoraireDto creneauHoraireDto;
    private Long matiereTeacherId;
    private MatiereTeacherDto matiereTeacher;

    private List<SeanceDto> seanceDtos = new ArrayList<>();

    public CoursDto createDTO(Cours cours) {

        if(cours != null){
            CoursDto coursDto = new CoursDto();
            coursDto.setId(cours.getId());
            coursDto.setJour(cours.getJour());

            if (cours.getCreneauHoraire() != null){
                coursDto.setCreneauHoraireId(cours.getCreneauHoraire().getId());
                coursDto.setCreneauHoraireDto(
                        new CreneauHoraireDto().createDTO(cours.getCreneauHoraire())
                );
            }

            if (cours.getMatiereTeacher() != null){
                coursDto.setMatiereTeacherId(cours.getMatiereTeacher().getId());
                coursDto.setMatiereTeacher(
                        new MatiereTeacherDto().createDTO(cours.getMatiereTeacher())
                );
            }

            if(cours.getSeances() != null){
                for (Seance seance : cours.getSeances()){
                    SeanceDto seanceDto = new SeanceDto().createDTO(seance);
                    seanceDtos.add(seanceDto);
                }
            }
            coursDto.setSeanceDtos(seanceDtos);

            return coursDto;
        }
        return null;
    }
}
