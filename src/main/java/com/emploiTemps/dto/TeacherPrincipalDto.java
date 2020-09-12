package com.emploiTemps.dto;


import com.emploiTemps.domain.TeacherPrincipal;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by yvano.
 */
@Data
public class TeacherPrincipalDto implements Serializable {

    private Long id;
    private Long teacherId;
    private String teacherCompleteName;

    public TeacherPrincipalDto createDTO(TeacherPrincipal teacherPrincipal) {

        if(teacherPrincipal != null){

            TeacherPrincipalDto teacherPrincipalDto = new TeacherPrincipalDto();
            teacherPrincipalDto.setId(teacherPrincipal.getId());

            if (teacherPrincipal.getTeacher() != null){
                teacherPrincipalDto.setTeacherId(teacherPrincipal.getTeacher().getId());
                teacherPrincipalDto.setTeacherCompleteName(teacherPrincipal.getTeacher().getCompleteName());
            }

            return teacherPrincipalDto;
        }
        return null;
    }

}
