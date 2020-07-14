/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myschool.domain;

import com.myschool.domain.enumerations.TypeEtablissement;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

/**
 *
 * @author medilox
 */
@Entity
@Data
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

    @Size(max = 20)
    @Column(name = "typeEtablissement")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String typeEtablissement;

    @Column(name = "nomEtablissement")
    private String nomEtablissement;

    @Column(name = "pays")
    private String pays;

    @Column(name = "ville")
    private String ville;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "senderId")
    private String senderId;

    @JoinColumn(name = "bulletin_id", referencedColumnName = "id")
    @ManyToOne
    private Bulletin bulletin;

    @JoinColumn(name = "carte_scolaire_id", referencedColumnName = "id")
    @ManyToOne
    private CarteScolaire carteScolaire;

    @JoinColumn(name = "tableau_honneur_id", referencedColumnName = "id")
    @ManyToOne
    private TableauHonneur tableauHonneur;

    @JoinColumn(name = "recu_id", referencedColumnName = "id")
    @ManyToOne
    private Recu recu;

    @Transient
    public TypeEtablissement getTypeEtablissement() {
        return TypeEtablissement.fromValue(typeEtablissement);
    }

    public void setTypeEtablissement(TypeEtablissement typeEtablissement) {
        this.typeEtablissement = typeEtablissement.toValue();
    }

    @Override
    public String toString() {
        return this.debut + " - " + this.fin;
    }
}
