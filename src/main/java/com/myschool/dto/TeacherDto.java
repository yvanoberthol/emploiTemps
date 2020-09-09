package com.myschool.dto;

import com.myschool.domain.MatiereTeacher;
import com.myschool.domain.Promo;
import com.myschool.domain.TeacherPrincipal;
import lombok.Data;

import java.util.List;

/**
 * Created by yvano.
 */
@Data
public class TeacherDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String sexe;
    private String nationalite;
    private String domicile;
    private Promo promo;

    private List<MatiereTeacher> matiereTeachers;
    private TeacherPrincipal teacherPrincipal;

    public String getCompleteName(){
        return firstName +' '+lastName;
    }
}
