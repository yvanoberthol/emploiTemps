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
@Table(name = "annee")
public class Annee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(name = "debut")
    private Integer debut;

    @Column(name = "fin")
    private Integer fin;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "annee")
    private List<Promo> promos;

    @Column(name = "nomEtablissement")
    private String nomEtablissement;

    @Column(name = "nameEtablissement")
    private String nameEtablissement;

    @Column(name = "slogan")
    private String slogan;

    @Column(name = "phone")
    private String phone;

    @Column(name = "pays")
    private String pays;

    @Column(name = "region")
    private String region;

    @Column(name = "departement")
    private String departement;

    @Column(name = "ville")
    private String ville;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "senderId")
    private String senderId;


    public String getSessionYear(){
        return this.debut.toString() + " - " + this.fin.toString();
    }
}
