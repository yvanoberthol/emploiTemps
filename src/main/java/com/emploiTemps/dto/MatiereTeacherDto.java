package com.emploiTemps.dto;

import com.emploiTemps.domain.MatiereTeacher;
import lombok.Data;

import java.io.Serializable;

@Data
public class MatiereTeacherDto implements Serializable {

    private Long id;
    private Long matiereId;
    private String matiereName;
    private MatiereDto matiereDto;
    private TeacherDto teacherDto;

    public MatiereTeacherDto createDTO(MatiereTeacher matiereTeacher) {

        if (matiereTeacher != null){
            MatiereTeacherDto matiereTeacherDto = new MatiereTeacherDto();
            matiereTeacherDto.setId(matiereTeacher.getId());

            if (matiereTeacher.getMatiere() != null){
                matiereTeacherDto.setMatiereId(matiereTeacher.getMatiere().getId());
                matiereTeacherDto.setMatiereName(matiereTeacher.getMatiere().getName());
                matiereTeacherDto.setMatiereDto(
                        new MatiereDto().createDTO(matiereTeacher.getMatiere())
                );
            }

            if (matiereTeacher.getTeacher() != null){
                matiereTeacherDto.setTeacherDto(
                        new TeacherDto().createDTO(matiereTeacher.getTeacher())
                );
            }
            return matiereTeacherDto;
        }
        return null;

    }
}
