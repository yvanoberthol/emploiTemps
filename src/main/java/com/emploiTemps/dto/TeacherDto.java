package com.emploiTemps.dto;

import com.emploiTemps.domain.Teacher;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by yvano.
 */
@Data
public class TeacherDto implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private String sexe;
    private String telephone;
    private String nationalite;
    private String domicile;

    public String getCompleteName(){
        return firstName +' '+lastName;
    }

    public TeacherDto createDTO(Teacher teacher) {

        if(teacher != null){
            TeacherDto teacherDto = new TeacherDto();
            teacherDto.setId(teacher.getId());
            teacherDto.setFirstName(teacher.getFirstName());
            teacherDto.setLastName(teacher.getLastName());
            //-------------------------------------------------
            teacherDto.setSexe(String.valueOf(teacher.getSexe()));
            teacherDto.setNationalite(teacher.getNationalite());
            teacherDto.setTelephone(teacher.getTelephone());
            teacherDto.setNationalite(teacher.getNationalite());
            teacherDto.setDomicile(teacher.getDomicile());

            return teacherDto;
        }
        return null;
    }
}
