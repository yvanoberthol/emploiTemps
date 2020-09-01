/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myschool.domain;

import com.myschool.domain.enumerations.TypeCarte;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 *
 * @author medilox
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "carteScolaire")
public class CarteScolaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Size(max = 20)
    @Column(name = "typeCarte")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String typeCarte;

    @Transient
    public TypeCarte getTypeCarte() {
        return TypeCarte.fromValue(typeCarte);
    }

    public void setTypeCarte(TypeCarte typeCarte) {
        this.typeCarte = typeCarte.toValue();
    }

}
