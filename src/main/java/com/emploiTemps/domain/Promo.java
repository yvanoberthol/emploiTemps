/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emploiTemps.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 *
 * @author medilox
 */

@Entity
@Getter
@Setter
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
    @ManyToOne
    private Annee annee;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "promo")
    private List<Matiere> matieres;

    @ManyToMany(mappedBy = "promos")
    private List<Teacher> teachers;

    @Column(name = "montantScolarite")
    private double montantScolarite;

    @Column(name = "capacite")
    private int capacite;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "promo")
    private TeacherPrincipal teacherPrincipal;

    @Override
    public String toString() {
        return this.classe + " " + this.annee.toString();
    }
}

