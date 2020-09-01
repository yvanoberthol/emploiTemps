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
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author medilox
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inscription")
public class Inscription implements Comparable {

    @EmbeddedId
    protected InscriptionPK inscriptionPK;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "montantScolarite")
    private double montantScolarite;

    @Column(name = "solde")
    private double solde;

    @JoinColumn(name = "promo_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Promo promo;

    @JoinColumn(name = "student_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Student student;

    @JoinColumn(name = "staff_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User staff;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inscription")
    private List<Reglement> reglements;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inscription")
    private List<StudentNote> studentNotes;

    public Inscription(InscriptionPK inscriptionPK) {
        this.inscriptionPK = inscriptionPK;
    }

    public Inscription(long studentId, long promoId) {
        this.inscriptionPK = new InscriptionPK(studentId, promoId);
    }

    public String getCreatedDate() {
        String pattern = "yyyy-MM-dd HH:mm";
        if(createdDate != null) {
            return createdDate.toString(pattern);
        }
        return null;
    }

    public void setCreatedDate(String createdDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime cd = null;
        if(createdDate!=null)
            cd = LocalDateTime.parse(createdDate, formatter);
        this.createdDate = cd;
    }

    public int getMontantTotal() {
        int total = 0;
        for(Reglement reglement: this.getReglements()){
            total += reglement.getAmount();
        }
        return total;
    }

    @Override
    public int compareTo(Object o) {
        return this.getStudent().getLastName().compareToIgnoreCase( ((Inscription)o).getStudent().getLastName());
    }
}
