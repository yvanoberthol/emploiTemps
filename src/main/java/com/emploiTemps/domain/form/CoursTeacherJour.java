package com.emploiTemps.domain.form;


import com.emploiTemps.dto.CoursDto;
import com.emploiTemps.dto.SeanceDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoursTeacherJour implements Serializable {

    // champs
    private CoursDto cours;
    private SeanceDto seance;


}
