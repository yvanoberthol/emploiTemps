/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myschool.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 *
 * @author medilox
 */
@Entity
@Table(name = "sms_student")
@Data
@NoArgsConstructor
public class SmsStudent {
    @EmbeddedId
    protected SmsStudentPK smsStudentPK;

    @Column(name = "message")
    private String message;

    @JoinColumn(name = "student_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Student student;

    @JoinColumn(name = "sms_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Sms sms;

    public SmsStudent(SmsStudentPK smsStudentPK) {
        this.smsStudentPK = smsStudentPK;
    }

    public SmsStudent(long smsId, long studentId) {
        this.smsStudentPK = new SmsStudentPK(smsId, studentId);
    }
}
