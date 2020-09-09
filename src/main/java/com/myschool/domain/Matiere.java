/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myschool.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 *
 * @author medilox
 * updated by yvano
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "matiere")
public class Matiere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "coef")
    private Double coef;

    @Column(name = "noteSur")
    private Integer noteSur;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "matiere")
    private List<MatiereTeacher> matiereTeachers;

    @JoinColumn(name = "groupe_id", referencedColumnName = "id")
    @ManyToOne
    private Groupe groupe;

    @JoinColumn(name = "promo_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Promo promo;

    public Matiere(String name) {
        this.name = name;
    }
}
