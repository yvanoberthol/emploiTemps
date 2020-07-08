package com.myschool.dto;

import com.myschool.domain.Student;
import lombok.Data;

import java.util.Map;


/**
 * Created by medilox on 3/10/17.
 */
@Data
public class StudentWithNotesForMatiereDto {

    private Long id;
    private String name;
    private String firstName;
    private String lastName;
    private String matricule;
    private Map<Long, Double> notes;

    public StudentWithNotesForMatiereDto createDTO(Student student) {
        StudentWithNotesForMatiereDto studentDto = new StudentWithNotesForMatiereDto();

        if(student != null){
            studentDto.setId(student.getId());
            studentDto.setFirstName(student.getFirstName());
            studentDto.setLastName(student.getLastName());
            studentDto.setName(student.getName());
            studentDto.setMatricule(student.getMatricule());
        }
        return studentDto;
    }
}
