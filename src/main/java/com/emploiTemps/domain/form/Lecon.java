package com.emploiTemps.domain.form;

import com.emploiTemps.dto.CreneauHoraireDto;
import com.emploiTemps.dto.MatiereDto;
import com.emploiTemps.dto.TeacherDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class Lecon implements Serializable {

    private int jour;
    private CreneauHoraireDto creneauHoraire;
    private MatiereDto matiere;
    private TeacherDto teacher;
}
