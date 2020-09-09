package com.myschool.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 *
 * @author yvano
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "teacherPrincipal")
public class TeacherPrincipal {

    @OneToOne
    @JoinColumn(name = "promo_id", referencedColumnName = "id")
    private Promo promo;

    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Teacher teacher;


    public String getCompleteName(){
       return teacher.getCompleteName();
    }

    public String getClasse(){
        return promo.getClasse();
    }
}
