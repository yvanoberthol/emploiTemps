/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myschool.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 * @author medilox
 */
@Data
@NoArgsConstructor
@Embeddable
public class InscriptionPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "student_id")
    private long studentId;

    @Basic(optional = false)
    @NotNull
    @Column(name = "promo_id")
    private long promoId;

    public InscriptionPK(long studentId, long promoId) {
        this.studentId = studentId;
        this.promoId = promoId;
    }
}
