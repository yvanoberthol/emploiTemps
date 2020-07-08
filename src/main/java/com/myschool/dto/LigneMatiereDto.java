package com.myschool.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * Created by medilox on 3/10/17.
 */
@Data
@NoArgsConstructor
public class LigneMatiereDto {
    private String matiere;
    private String teacher;
    private Double coef;
    private Integer noteSur;
    private Integer rang;
    private Double moyenne;
    private String mention;
    private List<StudentNoteDto> notes;
}
