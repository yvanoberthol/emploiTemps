/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myschool.domain;
import com.myschool.domain.enumerations.PaymentMethod;
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
@Table(name = "reglement")
public class Reglement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private double amount;

    /*@JoinColumn(name = "student_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Student student;

    @JoinColumn(name = "promo_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Promo promo;*/

    @JoinColumns({
            @JoinColumn(name = "promo_id", referencedColumnName = "promo_id"),
            @JoinColumn(name = "student_id", referencedColumnName = "student_id")})
    @ManyToOne(optional = false)
    private Inscription inscription;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "payment_validated")
    private Boolean paymentValidated;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "payment_validated_date", nullable = true)
    private LocalDateTime paymentValidatedDate = null;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @JoinColumn(name = "staff_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User staff;

    @Transient
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.fromValue(paymentMethod);
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod.toValue();
    }

    public String getPaymentValidatedDate() {
        String pattern = "yyyy-MM-dd HH:mm";
        if(paymentValidatedDate != null) {
            return paymentValidatedDate.toString(pattern);
        }
        return null;
    }

    public void setPaymentValidatedDate(String paymentValidatedDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime edt = null;
        if(paymentValidatedDate!=null)
            edt = LocalDateTime.parse(paymentValidatedDate, formatter);
        this.paymentValidatedDate = edt;
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

    public String getPaymentDate() {
        String pattern = "yyyy-MM-dd HH:mm";
        if(paymentDate != null) {
            return paymentDate.toString(pattern);
        }
        return null;
    }

    public void setPaymentDate(String paymentDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime cd = null;
        if(paymentDate!=null)
            cd = LocalDateTime.parse(paymentDate, formatter);
        this.paymentDate = cd;
    }
}
