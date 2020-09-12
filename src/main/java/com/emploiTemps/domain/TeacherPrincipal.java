package com.emploiTemps.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 *
 * @author yvano
 */
@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "teacherPrincipal")
public class TeacherPrincipal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "promo_id",nullable = false)
    private Promo promo;

    @OneToOne(cascade = CascadeType.ALL,optional = false)
    @JoinColumn(name = "teacher_id",nullable = false)
    private Teacher teacher;

    private Integer Annee;

    public String getCompleteName(){
       return teacher.getCompleteName();
    }

    public String getClasse(){
        return promo.getClasse();
    }
}
