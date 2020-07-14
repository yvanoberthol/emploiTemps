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

/**
 *
 * @author medilox
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recu")
public class Recu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String backgroundUrl;

}
