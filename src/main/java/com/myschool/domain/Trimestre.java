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
@Table(name = "trimestre")
public class Trimestre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "rank")
    private Integer rank;

    @JoinColumn(name = "annee_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Annee annee;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trimestre")
    private List<Sequence> sequences;

    public Trimestre(String name, Integer rank) {
        this.name = name;
        this.rank = rank;
    }
}
