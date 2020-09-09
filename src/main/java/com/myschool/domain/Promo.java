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
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "promo")
public class Promo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String classe;

    @JoinColumn(name = "annee_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Annee annee;

    @OneToMany(mappedBy = "promo")
    private List<Inscription> inscriptions;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "promo")
    private List<Groupe> groupes;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "promo")
    private List<Matiere> matieres;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "promo")
    private List<Teacher> teachers;

    @Column(name = "montantScolarite")
    private double montantScolarite;

    @Column(name = "capacite")
    private int capacite;

    @OneToOne(mappedBy = "promo")
    private TeacherPrincipal teacherPrincipal;


    @Override
    public String toString() {
        return this.classe + " " + this.annee.toString();
    }
}

