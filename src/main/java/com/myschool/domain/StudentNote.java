/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myschool.domain;

import lombok.*;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.*;

/**
 *
 * @author medilox
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "StudentNote")
public class StudentNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @JoinColumns({
            @JoinColumn(name = "promo_id", referencedColumnName = "promo_id"),
            @JoinColumn(name = "student_id", referencedColumnName = "student_id")})
    @ManyToOne(optional = false)
    private Inscription inscription;

    @JoinColumn(name = "sequence_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Sequence sequence;

    @JoinColumn(name = "matiere_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Matiere matiere;

    @Column(name = "note")
    private Double note;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    public String getLastUpdate() {
        String pattern = "yyyy-MM-dd HH:mm";
        if(lastUpdate != null) {
            return lastUpdate.toString(pattern);
        }
        return null;
    }

    public void setLastUpdate(String lastUpdate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime cd = null;
        if(lastUpdate!=null)
            cd = LocalDateTime.parse(lastUpdate, formatter);
        this.lastUpdate = cd;
    }
}
