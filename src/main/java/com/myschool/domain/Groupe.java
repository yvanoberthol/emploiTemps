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
@Table(name = "groupe")
public class Groupe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "rank")
    private Integer rank;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "groupe")
    private List<Matiere> matieres;

    @JoinColumn(name = "promo_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Promo promo;

    public Groupe(String name) {
        this.name = name;
    }

    public boolean isDeletable() {
        if(!this.getMatieres().isEmpty()){
            return false;
        }
        return true;
    }
}
